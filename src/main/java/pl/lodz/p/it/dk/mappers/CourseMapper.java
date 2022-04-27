package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;

@Mapper
public interface CourseMapper {

    @Mappings({
            @Mapping(target = "accountId", source = "trainee.account.id"),
            @Mapping(target = "courseDetailsId", source = "courseDetails.id"),
            @Mapping(target = "lectureGroupId", source = "lectureGroup.id")
    })
    CourseDto toCourseDto(Course course);
}