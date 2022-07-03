package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.dk.entities.CourseDetails;
import pl.lodz.p.it.dk.mos.dtos.CourseDetailsDto;

@Mapper
public interface CourseDetailsMapper {

    CourseDetailsDto toCourseDetailsDto(CourseDetails courseDetails);
}
