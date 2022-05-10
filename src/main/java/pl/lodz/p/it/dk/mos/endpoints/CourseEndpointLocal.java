package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

@Local
public interface CourseEndpointLocal extends TransactionStarter {

    @RolesAllowed("createCourse")
    void createCourse(CourseCategory courseCategory) throws BaseException;

    @RolesAllowed("getOwnCourse")
    public CourseDto getOwnCourse() throws BaseException;

    @RolesAllowed("getOtherCourse")
    public CourseDto getOtherCourse(String login) throws BaseException;

    @RolesAllowed("getTraineeForGroup")
    public List<TraineeForGroupDto> getTraineesForGroup(CourseCategory courseCategory) throws BaseException;
}
