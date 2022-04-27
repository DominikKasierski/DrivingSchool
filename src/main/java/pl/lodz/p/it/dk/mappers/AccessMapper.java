package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.mok.dtos.AccessesDto;
import pl.lodz.p.it.dk.mok.dtos.SingleAccessDto;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface AccessMapper {

    @Mappings({
            @Mapping(target = "accessType", source = "accessType"),
            @Mapping(target = "version", source = "version")
    })
    SingleAccessDto toAccessDto(Access access);

    @Mappings({
            @Mapping(target = "coursesDto", source = "courses", qualifiedByName = {"mapCourses"}),
            @Mapping(target = "id", source = "account.id")
    })
    TraineeAccessDto toTraineeAccessDto(TraineeAccess traineeAccess);

    @Named("mapCourses")
    default Set<CourseDto> mapCourses(Set<Course> courses) {
        Set<CourseDto> coursesDto = new HashSet<>();
        courses.forEach(x -> coursesDto.add(Mappers.getMapper(CourseMapper.class).toCourseDto(x)));
        return coursesDto;
    }

    default AccessesDto toAccessesDto(Account account) {
        List<SingleAccessDto> accessesGranted = account.getAccesses().stream()
                .filter(Access::isActivated)
                .map(this::toAccessDto)
                .collect(Collectors.toList());
        List<SingleAccessDto> accessesRevoked = account.getAccesses().stream()
                .filter(x -> !x.isActivated())
                .map(this::toAccessDto)
                .collect(Collectors.toList());
        return new AccessesDto(account.getId(), accessesGranted, accessesRevoked);
    }
}
