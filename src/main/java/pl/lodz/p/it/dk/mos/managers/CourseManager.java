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

    @RolesAllowed({"getOwnCourse", "getOtherCourse"})
    public Course getOngoingCourse(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        TraineeAccess traineeAccess = traineeAccessManager.find(account);
        return courseFacade.findByTraineeId(traineeAccess.getId());
    }
}
