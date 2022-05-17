package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DrivingLessonException;
import pl.lodz.p.it.dk.exceptions.LectureGroupException;
import pl.lodz.p.it.dk.mos.facades.DrivingLessonFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DrivingLessonManager {

    @Inject
    AccountManager accountManager;

    @Inject
    InstructorAccessManager instructorAccessManager;

    @Inject
    CourseManager courseManager;

    @Inject
    private CarManager carManager;

    @Inject
    CourseDetailsManager courseDetailsManager;

    @Inject
    DrivingLessonFacade drivingLessonFacade;

    @Inject
    private EmailService emailService;

    @RolesAllowed("addDrivingLesson")
    public void addDrivingLesson(Course course, int numberOfHours, Date dateFrom, String instructorLogin)
            throws BaseException {

        if (!course.isPaid() || !course.isLecturesCompletion()) {
            throw DrivingLessonException.conditionsNotMet();
        }

        Date truncatedDateFrom = new Date(dateFrom
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.HOURS)
                .toInstant()
                .toEpochMilli());
        Date endDate = checkHoursConditionsAndReturnEndDate(truncatedDateFrom, numberOfHours);

        Account account = accountManager.findByLogin(instructorLogin);
        InstructorAccess instructorAccess = instructorAccessManager.find(account);

        if (!instructorAccess.getPermissions().contains(course.getCourseDetails().getCourseCategory())) {
            throw DrivingLessonException.noPermits();
        }

        for (Lecture lecture : instructorAccess.getLectures()) {
            ifTwoDateRangesOverlap(truncatedDateFrom, endDate, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : instructorAccess.getDrivingLessons()) {
            ifTwoDateRangesOverlap(truncatedDateFrom, endDate, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
        }

        for (Lecture lecture : course.getLectureGroup().getLectures()) {
            ifTwoDateRangesOverlap(truncatedDateFrom, endDate, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : course.getDrivingLessons()) {
            ifTwoDateRangesOverlap(truncatedDateFrom, endDate, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
        }

        checkNumberOfHours(course, truncatedDateFrom, endDate, course.getCreatedBy());

        Car car =
                carManager.findAvailableCar(truncatedDateFrom, endDate, course.getCourseDetails().getCourseCategory());
        DrivingLesson drivingLesson = new DrivingLesson(instructorAccess, course, car, truncatedDateFrom, endDate);
        drivingLesson.setCreatedBy(course.getCreatedBy());

        course.getDrivingLessons().add(drivingLesson);
        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(course.getCreatedBy());
        courseManager.edit(course);

        instructorAccess.getDrivingLessons().add(drivingLesson);
        instructorAccess.setModificationDate(Date.from(Instant.now()));
        instructorAccess.setModifiedBy(course.getCreatedBy());
        instructorAccessManager.edit(instructorAccess);

        car.getDrivingLessons().add(drivingLesson);
        car.setModificationDate(Date.from(Instant.now()));
        car.setModifiedBy(course.getCreatedBy());
        carManager.edit(car);
    }

    @RolesAllowed("cancelDrivingLesson")
    public void cancelDrivingLesson(Long id) throws BaseException {
        DrivingLesson drivingLesson = drivingLessonFacade.find(id);

        Date now = new Date(new Date().getTime());
        long diffInMillis = drivingLesson.getDateFrom().getTime() - now.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < 2) {
            throw DrivingLessonException.timeForCancellationExceeded();
        }

        if (drivingLesson.getLessonStatus().equals(LessonStatus.PENDING)) {
            drivingLesson.setLessonStatus(LessonStatus.CANCELLED);
            drivingLesson.setModificationDate(Date.from(Instant.now()));
            drivingLesson.setModifiedBy(drivingLesson.getCreatedBy());
            drivingLessonFacade.edit(drivingLesson);
            emailService.sendDrivingLessonCancellationEmail(drivingLesson.getInstructor().getAccount(),
                    drivingLesson.getDateFrom().toString());
        } else {
            throw DrivingLessonException.incorrectLessonStatus();
        }
    }

    private Date checkHoursConditionsAndReturnEndDate(Date dateToCheck, int numberOfHours)
            throws DrivingLessonException {
        Date now = new Date(new Date().getTime());
        long diffInMillis = dateToCheck.getTime() - now.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < 3) {
            throw DrivingLessonException.timeForAddingExceeded();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(dateToCheck);
        int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) {
            throw DrivingLessonException.invalidDateRange();
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(dateToCheck);
        endCalendar.add(Calendar.HOUR_OF_DAY, numberOfHours);

        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

        if (startHour >= 8 && endHour <= 16) {
            return endCalendar.getTime();
        } else {
            throw DrivingLessonException.invalidDateRange();
        }
    }

    private void ifTwoDateRangesOverlap(Date startA, Date endA, Date startB, Date endB) throws LectureGroupException {
        if ((startA.getTime() < endB.getTime() && startB.getTime() < endA.getTime()) ||
                (startA.getTime() == startB.getTime())) {
            throw LectureGroupException.dateRangesOverlap();
        }
    }

    private void checkNumberOfHours(Course course, Date from, Date to, Account adminAccount) throws BaseException {
        long totalNumberOfHours = 0;
        long drivingHoursLimit =
                courseDetailsManager.findByCategory(course.getCourseDetails().getCourseCategory()).getDrivingHours();

        for (DrivingLesson drivingLesson : course.getDrivingLessons()) {
            if (!drivingLesson.getLessonStatus().equals(LessonStatus.CANCELLED)) {
                long diffInMillis = drivingLesson.getDateTo().getTime() - drivingLesson.getDateFrom().getTime();
                totalNumberOfHours += TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            }
        }

        totalNumberOfHours += TimeUnit.HOURS.convert((to.getTime() - from.getTime()), TimeUnit.MILLISECONDS);

        if (totalNumberOfHours > drivingHoursLimit) {
            throw DrivingLessonException.tooManyDrivingHours();
        }
    }
}
