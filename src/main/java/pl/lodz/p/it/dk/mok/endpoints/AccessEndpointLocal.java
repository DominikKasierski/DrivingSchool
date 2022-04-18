package pl.lodz.p.it.dk.mok.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface AccessEndpointLocal extends TransactionStarter {

    @RolesAllowed("grantAccessType")
    void grantAccessType(String login, AccessType accessType) throws BaseException;

    @RolesAllowed("revokeAccessType")
    void revokeAccessType(String login, AccessType accessType) throws BaseException;

    @PermitAll
    void switchAccessType(AccessType accessType);
}
