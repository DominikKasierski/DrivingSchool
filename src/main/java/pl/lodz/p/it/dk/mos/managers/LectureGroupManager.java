package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.LectureGroupException;
import pl.lodz.p.it.dk.mos.dtos.EventDto;
import pl.lodz.p.it.dk.mos.dtos.CalendarDto;
import pl.lodz.p.it.dk.mos.facades.LectureGroupFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class LectureGroupManager {

    @Inject
    AccountManager accountManager;

    @Inject
    CourseManager courseManager;

    @Inject
    InstructorAccessManager instructorAccessManager;

    @Inject
    CourseDetailsManager courseDetailsManager;

    @Inject
    LectureGroupFacade lectureGroupFacade;

    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(LectureGroup lectureGroup, String login) throws BaseException {
        lectureGroup.setCreatedBy(accountManager.findByLogin(login));
        lectureGroupFacade.create(lectureGroup);
    }

    @RolesAllowed("getLectureGroups")
    public List<LectureGroup> getOpenLectureGroups() throws BaseException {
        List<LectureGroup> lectureGroups = lectureGroupFacade.findAll();
        List<LectureGroup> openLectureGroups = new ArrayList<>();

        for (LectureGroup lectureGroup : lectureGroups) {
            if (lectureGroup.getCourses().isEmpty() ||
                    !lectureGroup.getCourses().iterator().next().isLecturesCompletion()) {
                openLectureGroups.add(lectureGroup);
            }
        }

        return openLectureGroups;
    }

    @RolesAllowed({"getLectureGroup", "addLectureForGroup", "getGroupCalendar"})
    public LectureGroup findById(Long id) throws BaseException {
        return lectureGroupFacade.find(id);
    }

    @RolesAllowed("assignToLectureGroup")
    public void assignToLectureGroup(LectureGroup lectureGroup, Long courseId, String login) throws BaseException {
        Course course = courseManager.findById(courseId);

        if (course.getLectureGroup() != null) {
            throw LectureGroupException.alreadyAssigned();
        } else if (!course.isAdvance()) {
            throw LectureGroupException.unpaidCourse();
        } else if (!lectureGroup.getLectures().isEmpty()) {
            throw LectureGroupException.lecturesStarted();
        }

        course.setLectureGroup(lectureGroup);
        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(accountManager.findByLogin(login));

        lectureGroup.getCourses().add(course);
        lectureGroup.setModificationDate(Date.from(Instant.now()));
        lectureGroup.setModifiedBy(accountManager.findByLogin(login));
        lectureGroupFacade.edit(lectureGroup);
    }

    @RolesAllowed("addLectureForGroup")
    public void addLectureForGroup(LectureGroup lectureGroup, Date dateFrom, Date dateTo, String instructorLogin,
                                   String adminLogin) throws BaseException {
        Date now = new Date(new Date().getTime());

        if (!dateFrom.before(dateTo) || now.after(dateFrom)) {
            throw LectureGroupException.invalidDateRange();
        }

        long diffInMillis = dateFrom.getTime() - now.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < 3) {
            throw LectureGroupException.timeForAddingExceeded();
        }

        Account account = accountManager.findByLogin(instructorLogin);
        InstructorAccess instructorAccess = instructorAccessManager.find(account);

        if (!instructorAccess.getPermissions().contains(lectureGroup.getCourseCategory())) {
            throw LectureGroupException.noPermits();
        }

        checkAvailabilityOfDate(lectureGroup, dateFrom, dateTo, instructorAccess);

        Account adminAccount = accountManager.findByLogin(adminLogin);
        checkNumberOfHours(lectureGroup, dateFrom, dateTo, adminAccount);

        Lecture lecture = new Lecture(instructorAccess, lectureGroup, dateFrom, dateTo);
        lecture.setCreatedBy(adminAccount);

        lectureGroup.getLectures().add(lecture);
        lectureGroup.setModificationDate(Date.from(Instant.now()));
        lectureGroup.setModifiedBy(adminAccount);
        lectureGroupFacade.edit(lectureGroup);

        instructorAccess.getLectures().add(lecture);
        instructorAccess.setModificationDate(Date.from(Instant.now()));
        instructorAccess.setModifiedBy(adminAccount);
        instructorAccessManager.edit(instructorAccess);
    }

    @RolesAllowed("getGroupCalendar")
    public CalendarDto getGroupCalendar(LectureGroup lectureGroup, Long from) throws BaseException {
        Calendar dateFrom = Calendar.getInstance();
        dateFrom.setTime(new Date(from * 1000));

        List<EventDto> mondayEvents = getEventsForDay(lectureGroup, dateFrom);
        List<EventDto> tuesdayEvents = getEventsForDay(lectureGroup, dateFrom);
        List<EventDto> wednesdayEvents = getEventsForDay(lectureGroup, dateFrom);
        List<EventDto> thursdayEvents = getEventsForDay(lectureGroup, dateFrom);
        List<EventDto> fridayEvents = getEventsForDay(lectureGroup, dateFrom);
        List<EventDto> saturdayEvents = getEventsForDay(lectureGroup, dateFrom);

        return new CalendarDto(mondayEvents, tuesdayEvents, wednesdayEvents, thursdayEvents, fridayEvents, saturdayEvents,
                new ArrayList<>());
    }

    private void checkAvailabilityOfDate(LectureGroup lectureGroup, Date dateFrom, Date dateTo,
                                         InstructorAccess instructorAccess) throws LectureGroupException {
        for (Lecture lecture : lectureGroup.getLectures()) {
            ifTwoDateRangesOverlap(dateFrom, dateTo, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (Lecture lecture : instructorAccess.getLectures()) {
            ifTwoDateRangesOverlap(dateFrom, dateTo, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : instructorAccess.getDrivingLessons()) {
            ifTwoDateRangesOverlap(dateFrom, dateTo, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
        }
    }

    private void ifTwoDateRangesOverlap(Date startA, Date endA, Date startB, Date endB) throws LectureGroupException {
        if ((startA.getTime() < endB.getTime() && startB.getTime() < endA.getTime()) ||
                (startA.getTime() == startB.getTime())) {
            throw LectureGroupException.dateRangesOverlap();
        }
    }

    private void checkNumberOfHours(LectureGroup lectureGroup, Date from, Date to, Account adminAccount)
            throws BaseException {
        long totalNumberOfHours = 0;
        long lectureHoursLimit =
                courseDetailsManager.findByCategory(lectureGroup.getCourseCategory()).getLecturesHours();

        for (Lecture lecture : lectureGroup.getLectures()) {
            long diffInMillis = lecture.getDateTo().getTime() - lecture.getDateFrom().getTime();
            totalNumberOfHours += TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        }

        totalNumberOfHours += TimeUnit.HOURS.convert((to.getTime() - from.getTime()), TimeUnit.MILLISECONDS);

        if (totalNumberOfHours > lectureHoursLimit) {
            throw LectureGroupException.tooManyLectureHours();
        } else if (totalNumberOfHours == lectureHoursLimit) {
            for (Course x : lectureGroup.getCourses()) {
                x.setLecturesCompletion(true);
                x.setModificationDate(Date.from(Instant.now()));
                x.setModifiedBy(adminAccount);
                courseManager.edit(x);
            }
        }
    }

    private List<EventDto> getEventsForDay(LectureGroup lectureGroup, Calendar dateFrom) {
        List<EventDto> events = new ArrayList<>();

        for (Lecture lecture : lectureGroup.getLectures()) {
            if (checkIfDatesAreInTheSameDay(dateFrom, lecture.getDateFrom())) {
                Account instructorAccount = lecture.getInstructor().getAccount();
                String instructorDetails =
                        instructorAccount.getFirstname().concat(" ").concat(instructorAccount.getLastname());
                events.add(
                        new EventDto(lecture.getId(), "LECTURE", instructorDetails, "NaN", lecture.getDateFrom().getTime(),
                                lecture.getDateTo().getTime()));
            }
        }

        dateFrom.add(Calendar.DATE, 1);
        return events;
    }

    private boolean checkIfDatesAreInTheSameDay(Calendar dateFrom, Date lectureDate) {
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTime(lectureDate);

        return dateFrom.get(Calendar.DAY_OF_YEAR) == tmpCalendar.get(Calendar.DAY_OF_YEAR) &&
                dateFrom.get(Calendar.YEAR) == tmpCalendar.get(Calendar.YEAR);
    }
}
