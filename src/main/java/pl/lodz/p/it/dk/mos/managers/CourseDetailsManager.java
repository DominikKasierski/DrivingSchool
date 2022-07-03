package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.CourseDetails;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.facades.CourseDetailsFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class CourseDetailsManager {

    @Inject
    private CourseDetailsFacade courseDetailsFacade;

    @RolesAllowed({"createCourse", "addLectureForGroup", "addDrivingLesson"})
    public CourseDetails findByCategory(CourseCategory courseCategory) throws BaseException {
        return courseDetailsFacade.findByCategory(courseCategory);
    }

    @RolesAllowed("createCourse")
    public void edit(CourseDetails courseDetails) throws BaseException {
        courseDetailsFacade.edit(courseDetails);
    }

    @RolesAllowed("getCoursesDetails")
    public List<CourseDetails> getAllCoursesDetails() throws BaseException {
        return courseDetailsFacade.findAll();
    }
}
