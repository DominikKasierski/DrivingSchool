package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.LectureGroupException;
import pl.lodz.p.it.dk.mos.facades.LectureGroupFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @RolesAllowed({"getLectureGroup", "addLectureForGroup"})
    public LectureGroup findById(Long id) throws BaseException {
        return lectureGroupFacade.find(id);
    }

    @RolesAllowed("assignToLectureGroup")
    public void assignToLectureGroup(LectureGroup lectureGroup, Long courseId, String login) throws BaseException {
        Course course = courseManager.findById(courseId);

        if (course.getLectureGroup() != null) {
            throw LectureGroupException.alreadyAssigned();
        } else if (!course.isPaid()) {
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
        Date from = new Date(dateFrom.toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date to = new Date(dateTo.toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date now = new Date(new Date().toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        if (from.after(to) || now.after(from)) {
            throw LectureGroupException.invalidDateRange();
        }

        long diffInMillis = Math.abs(from.getTime() - now.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < 3) {
            throw LectureGroupException.timeForAddingExceeded();
        }

        Account account = accountManager.findByLogin(instructorLogin);
        InstructorAccess instructorAccess = instructorAccessManager.find(account);

        if (!instructorAccess.getPermissions().contains(lectureGroup.getCourseCategory())) {
            throw LectureGroupException.noPermits();
        }

        for (Lecture lecture : lectureGroup.getLectures()) {
            ifTwoDateRangesOverlap(from, to, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (Lecture lecture : instructorAccess.getLectures()) {
            ifTwoDateRangesOverlap(from, to, lecture.getDateFrom(), lecture.getDateTo());
        }

        for (DrivingLesson drivingLesson : instructorAccess.getDrivingLessons()) {
            ifTwoDateRangesOverlap(from, to, drivingLesson.getDateFrom(), drivingLesson.getDateTo());
        }

        Account adminAccount = accountManager.findByLogin(adminLogin);
        checkNumberOfHours(lectureGroup, from, to, adminAccount);

        Lecture lecture = new Lecture(instructorAccess, lectureGroup, from, to);
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

    private void ifTwoDateRangesOverlap(Date startA, Date endA, Date startB, Date endB) throws LectureGroupException {
        if (!(startA.getTime() <= endB.getTime() && endA.getTime() >= startB.getTime())) {
            throw LectureGroupException.dateRangesOverlap();
        }
    }

    private void checkNumberOfHours(LectureGroup lectureGroup, Date from, Date to, Account adminAccount)
            throws BaseException {
        long totalNumberOfHours = 0;
        long lectureHoursLimit =
                courseDetailsManager.findByCategory(lectureGroup.getCourseCategory()).getLecturesHours();

        for (Lecture lecture : lectureGroup.getLectures()) {
            long diffInMillis = lecture.getDateFrom().getTime() - lecture.getDateTo().getTime();
            totalNumberOfHours += TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        }

        totalNumberOfHours += TimeUnit.HOURS.convert((from.getTime() - to.getTime()), TimeUnit.MILLISECONDS);

        if (totalNumberOfHours > lectureHoursLimit) {
            throw LectureGroupException.tooManyLectureHours();
        } else if (totalNumberOfHours == lectureHoursLimit) {
            lectureGroup.getCourses().forEach(x -> {
                x.setCourseCompletion(true);
                x.setModificationDate(Date.from(Instant.now()));
                x.setModifiedBy(adminAccount);
            });
        }
    }
}
