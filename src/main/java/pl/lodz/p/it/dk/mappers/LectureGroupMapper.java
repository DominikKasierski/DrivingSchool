package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.mos.dtos.LectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;

@Mapper
public interface LectureGroupMapper {

    void toLectureGroup(NewLectureGroupDto newLectureGroupDto, @MappingTarget LectureGroup lectureGroup);

    LectureGroupDto toLectureGroupDto(LectureGroup lectureGroup);

}
