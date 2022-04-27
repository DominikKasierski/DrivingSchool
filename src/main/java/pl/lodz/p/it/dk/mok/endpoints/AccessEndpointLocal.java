package pl.lodz.p.it.dk.mok.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.AccessesDto;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface AccessEndpointLocal extends TransactionStarter {

    @RolesAllowed("grantAccessType")
    void grantAccessType(String login, AccessType accessType) throws BaseException;

    @RolesAllowed("revokeAccessType")
    void revokeAccessType(String login, AccessType accessType) throws BaseException;

    @RolesAllowed("getOwnAccesses")
    AccessesDto getOwnAccesses() throws BaseException;

    @RolesAllowed("getOtherAccesses")
    AccessesDto getOtherAccesses(String login) throws BaseException;

    @PermitAll
    void switchAccessType(AccessType accessType);

    @RolesAllowed("getTraineeAccess")
    TraineeAccessDto getTraineeAccess() throws BaseException;
}
