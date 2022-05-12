package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.LectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.NewLectureDto;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Local
public interface LectureGroupEndpointLocal extends TransactionStarter {

    @RolesAllowed("getTraineesForGroup")
    public List<TraineeForGroupDto> getTraineesForGroup(CourseCategory courseCategory) throws BaseException;

    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(NewLectureGroupDto newLectureGroupDto) throws BaseException;

    @RolesAllowed("getLectureGroups")
    public List<LectureGroupDto> getLectureGroups() throws BaseException;

    @RolesAllowed("getLectureGroup")
    public LectureGroupDto getLectureGroup(Long id) throws BaseException;

    @RolesAllowed("assignToLectureGroup")
    public void assignToLectureGroup(Long lectureGroupId, Long courseId) throws BaseException;

    @RolesAllowed("addLectureForGroup")
    public void addLectureForGroup(@NotNull @Valid NewLectureDto newLectureDto) throws BaseException;

}
