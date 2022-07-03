package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.CourseDetails;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CourseDetailsMapper;
import pl.lodz.p.it.dk.mos.dtos.CourseDetailsDto;
import pl.lodz.p.it.dk.mos.managers.CourseDetailsManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CourseDetailsEndpoint extends AbstractEndpoint implements CourseDetailsEndpointLocal {

    @Inject
    private CourseDetailsManager courseDetailsManager;

    @Override
    @RolesAllowed("getCoursesDetails")
    public List<CourseDetailsDto> getAllCoursesDetails() throws BaseException {
        List<CourseDetails> coursesDetails = courseDetailsManager.getAllCoursesDetails();
        List<CourseDetailsDto> coursesDetailsDto = new ArrayList<>();

        for (CourseDetails courseDetails : coursesDetails) {
            coursesDetailsDto.add(Mappers.getMapper(CourseDetailsMapper.class).toCourseDetailsDto(courseDetails));
        }

        return coursesDetailsDto;
    }
}
