package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.CourseDetails;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.CourseException;
import pl.lodz.p.it.dk.mos.facades.CourseFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourseManager {

    @Inject
    CourseDetailsManager courseDetailsManager;

    @Inject
    TraineeAccessManager traineeAccessManager;

    @Inject
    AccountManager accountManager;

    @Inject
    CourseFacade courseFacade;

    @RolesAllowed("createCourse")
    public void createCourse(CourseCategory courseCategory, TraineeAccess traineeAccess) throws BaseException {
        boolean hasOngoingCourse = traineeAccess.getCourses().stream()
                .anyMatch(x -> !x.isCourseCompletion());
        boolean hasSameCourseCompleted = traineeAccess.getCourses().stream()
                .anyMatch(x -> x.getCourseDetails().getCourseCategory().equals(courseCategory));

        if (hasOngoingCourse) {
            throw CourseException.alreadyAssigned();
        } else if (hasSameCourseCompleted) {
            throw CourseException.alreadyCompleted();
        }

        CourseDetails courseDetails = courseDetailsManager.findByCategory(courseCategory);
        Course course = new Course(traineeAccess, courseDetails);
        course.setCreatedBy(traineeAccess.getAccount());

        traineeAccess.getCourses().add(course);
        traineeAccess.setModificationDate(Date.from(Instant.now()));
        traineeAccess.setModifiedBy(traineeAccess.getAccount());

        courseDetails.getCourses().add(course);
        courseDetails.setModificationDate(Date.from(Instant.now()));
        courseDetails.setModifiedBy(traineeAccess.getAccount());

        traineeAccessManager.edit(traineeAccess);
        courseDetailsManager.edit(courseDetails);
    }

    @RolesAllowed({"createPayment", "cancelPayment", "confirmPayment", "rejectPayment"})
    public Course getOngoingCourse(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        TraineeAccess traineeAccess = traineeAccessManager.find(account);
        List<Course> courses = courseFacade.findByTraineeId(traineeAccess.getId());

        return courses.stream()
                .filter(x -> !x.isCourseCompletion())
                .findAny()
                .orElseThrow(CourseException::noOngoingCourse);
    }

    @RolesAllowed("createPayment")
    public Course findById(Long id) throws BaseException {
        return courseFacade.find(id);
    }

    @RolesAllowed({"createPayment", "cancelPayment", "confirmPayment", "rejectPayment"})
    public void edit(Course course) throws BaseException {
        courseFacade.edit(course);
    }

    @RolesAllowed("getUnderpayments")
    public List<Course> findByCategory(CourseCategory courseCategory) throws BaseException {
        return courseFacade.findByCategory(courseCategory);
    }

    @RolesAllowed("getTraineesForGroup")
    public List<Course> getPaidCoursesWithoutLectureGroup(CourseCategory courseCategory) throws BaseException {
        List<Course> courses = courseFacade.findByCategory(courseCategory);
        return courses.stream()
                .filter(x -> x.isPaid() && x.getLectureGroup() == null)
                .collect(Collectors.toList());
    }

}
