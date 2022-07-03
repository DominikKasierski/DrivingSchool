package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CourseDetailsDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

@Local
public interface CourseDetailsEndpointLocal extends TransactionStarter {

    @RolesAllowed("getCoursesDetails")
    List<CourseDetailsDto> getAllCoursesDetails() throws BaseException;
}
