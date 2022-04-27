package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourseManager {

    @Inject
    CourseDetailsManager courseDetailsManager;

    @RolesAllowed("createCourse")
    public void createCourse(CourseCategory courseCategory, TraineeAccess traineeAccess) throws BaseException {
        boolean ongoingCourses = traineeAccess.getCourses().stream()
                .anyMatch(x -> !x.isCourseCompletion());
        boolean sameCategoryCompletedCourse = traineeAccess.getCourses().stream()
                .anyMatch(x -> !x.isCourseCompletion());
    }
}
