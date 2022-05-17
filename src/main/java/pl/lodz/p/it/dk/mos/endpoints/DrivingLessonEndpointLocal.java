package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewDrivingLesson;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface DrivingLessonEndpointLocal extends TransactionStarter {

    @RolesAllowed("addDrivingLesson")
    public void addDrivingLesson(NewDrivingLesson newDrivingLesson) throws BaseException;
}
