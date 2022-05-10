package pl.lodz.p.it.dk.mappers;

import org.mapstruct.MappingTarget;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;

public interface LectureGroupMapper {

    void toLectureGroup(NewLectureGroupDto newLectureGroupDto, @MappingTarget LectureGroup lectureGroup);

}
