package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.NewDrivingLesson;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.DrivingLessonManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class DrivingLessonEndpoint extends AbstractEndpoint implements DrivingLessonEndpointLocal {

    @Inject
    CourseManager courseManager;

    @Inject
    DrivingLessonManager drivingLessonManager;

    @Override
    @RolesAllowed("addDrivingLesson")
    public void addDrivingLesson(NewDrivingLesson newDrivingLesson) throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        drivingLessonManager.addDrivingLesson(course, newDrivingLesson.getNumberOfHours(), newDrivingLesson.getDateFrom(),
                newDrivingLesson.getInstructorLogin());
    }
}
