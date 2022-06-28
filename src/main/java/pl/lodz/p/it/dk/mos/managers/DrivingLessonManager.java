package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DrivingLessonException;
import pl.lodz.p.it.dk.mos.facades.DrivingLessonFacade;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@DeclareRoles("Trainee")
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
    private SecurityContext securityContext;

    @Inject
    private EmailService emailService;

    @RolesAllowed("addDrivingLesson")
    public void addDrivingLesson(Course course, int numberOfHours, Date dateFrom, String instructorLogin)
            throws BaseException {

        if (!course.isAdvance() || !course.isLecturesCompletion()) {
            throw DrivingLessonException.conditionsNotMet();
        }

        Date truncatedDateFrom = new Date(dateFrom
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.HOURS)
                .toInstant()
                .toEpochMilli());
        checkStartDateRequirements(truncatedDateFrom, numberOfHours);

        Date endDate = getDrivingLessonEndDate(truncatedDateFrom, numberOfHours);
        Account account = accountManager.findByLogin(instructorLogin);
        InstructorAccess instructorAccess = instructorAccessManager.find(account);
        CourseCategory courseCategory = course.getCourseDetails().getCourseCategory();

        if (!instructorAccess.getPermissions().contains(courseCategory)) {
            throw DrivingLessonException.noPermits();
        }

        checkAvailabilityOfDate(course, truncatedDateFrom, endDate, instructorAccess);
        checkNumberOfDrivingLessonsHours(courseCategory, course.getDrivingLessons(), truncatedDateFrom, endDate,
                course.getCreatedBy());

        Car car = carManager.findAvailableCar(truncatedDateFrom, endDate, courseCategory);
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
    public void cancelDrivingLesson(Long id, String login) throws BaseException {
        DrivingLesson drivingLesson = drivingLessonFacade.find(id);
        Course course = drivingLesson.getCourse();
        String traineeLogin = course.getTrainee().getAccount().getLogin();
        String instructorLogin = drivingLesson.getInstructor().getAccount().getLogin();

        if (!login.equals(traineeLogin) && !login.equals(instructorLogin)) {
            throw DrivingLessonException.accessDenied();
        }

        if (!drivingLesson.getLessonStatus().equals(LessonStatus.PENDING)) {
            throw DrivingLessonException.incorrectLessonStatus();
        }

        Date now = new Date(new Date().getTime());
        long diffInMillis = drivingLesson.getDateFrom().getTime() - now.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (securityContext.isCallerInRole("Trainee") && diffInDays < 2) {
            throw DrivingLessonException.timeForCancellationExceeded();
        }

        drivingLesson.setLessonStatus(LessonStatus.CANCELLED);
        drivingLesson.setModificationDate(Date.from(Instant.now()));
        drivingLesson.setModifiedBy(accountManager.findByLogin(login));
        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(accountManager.findByLogin(login));
        courseManager.edit(course);

        if (securityContext.isCallerInRole("Trainee")) {
            emailService.sendDrivingLessonCancellationEmail(accountManager.findByLogin(instructorLogin),
                    drivingLesson.getDateFrom().toString());
        } else {
            emailService.sendDrivingLessonCancellationEmail(accountManager.findByLogin(traineeLogin),
                    drivingLesson.getDateFrom().toString());
        }
    }

    @RolesAllowed("cancelDrivingLesson")
    public DrivingLesson getDrivingLessonById(Long id) throws BaseException {
        return drivingLessonFacade.find(id);
    }

    private void checkStartDateRequirements(Date dateToCheck, int numberOfHours) throws DrivingLessonException {
        Date now = new Date(new Date().getTime());
        long diffInMillis = dateToCheck.getTime() - now.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < 3) {
            throw DrivingLessonException.timeForAddingExceeded();
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(dateToCheck);
        int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int endHour = startHour + numberOfHours;

        if (dayOfWeek == 1) {
            throw DrivingLessonException.invalidDateRange();
        } else if (startHour < 8 || endHour > 16) {
            throw DrivingLessonException.invalidDateRange();
        }
    }

    private Date getDrivingLessonEndDate(Date truncatedDateFrom, int numberOfHours) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(truncatedDateFrom);
        endCalendar.add(Calendar.HOUR_OF_DAY, numberOfHours);
        return endCalendar.getTime();
    }

    private void checkAvailabilityOfDate(Course course, Date dateFrom, Date endDate, InstructorAccess instructorAccess)
            throws DrivingLessonException {

        for (Lecture lecture : instructorAccess.getLectures()) {
            ifTwoDateRangesOverlap(dateFrom, endDate, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : instructorAccess.getDrivingLessons()) {
            if (drivingLesson.getLessonStatus().equals(LessonStatus.PENDING)) {
                ifTwoDateRangesOverlap(dateFrom, endDate, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
            }
        }

        for (Lecture lecture : course.getLectureGroup().getLectures()) {
            ifTwoDateRangesOverlap(dateFrom, endDate, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : course.getDrivingLessons()) {
            if (drivingLesson.getLessonStatus().equals(LessonStatus.PENDING)) {
                ifTwoDateRangesOverlap(dateFrom, endDate, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
            }
        }
    }

    private void ifTwoDateRangesOverlap(Date startA, Date endA, Date startB, Date endB) throws DrivingLessonException {
        if ((startA.getTime() < endB.getTime() && startB.getTime() < endA.getTime()) ||
                (startA.getTime() == startB.getTime())) {
            throw DrivingLessonException.dateRangesOverlap();
        }
    }

    private void checkNumberOfDrivingLessonsHours(CourseCategory courseCategory, Set<DrivingLesson> drivingLessons,
                                                  Date from, Date to, Account adminAccount) throws BaseException {
        long totalNumberOfHours = 0;
        long drivingHoursLimit = courseDetailsManager.findByCategory(courseCategory).getDrivingHours();

        for (DrivingLesson drivingLesson : drivingLessons) {
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
