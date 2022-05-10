package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

@Local
public interface LectureGroupEndpointLocal extends TransactionStarter {

    @RolesAllowed("getTraineeForGroup")
    public List<TraineeForGroupDto> getTraineesForGroup(CourseCategory courseCategory) throws BaseException;

    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(NewLectureGroupDto newLectureGroupDto) throws BaseException;
}
