package pl.lodz.p.it.dk.mok.endpoints;

import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.AccessMapper;
import pl.lodz.p.it.dk.mok.dtos.AccessesDto;
import pl.lodz.p.it.dk.mok.managers.AccessManager;
import pl.lodz.p.it.dk.mok.managers.AccountManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Log
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccessEndpoint extends AbstractEndpoint implements AccessEndpointLocal {

    @Inject
    private AccessManager accessManager;

    @Inject
    private AccountManager accountManager;

    @Override
    @RolesAllowed("grantAccessType")
    public void grantAccessType(String login, AccessType accessType) throws BaseException {
        Account account = accountManager.findByLogin(login);
        AccessesDto accessesDto = Mappers.getMapper(AccessMapper.class).toAccessesDto(account);
        verifyEntityIntegrity(accessesDto);
        accessManager.grantAccessType(login, accessType, getLogin());
    }

    @Override
    @RolesAllowed("revokeAccessType")
    public void revokeAccessType(String login, AccessType accessType) throws BaseException {
        Account account = accountManager.findByLogin(login);
        AccessesDto accessesDto = Mappers.getMapper(AccessMapper.class).toAccessesDto(account);
        verifyEntityIntegrity(accessesDto);
        accessManager.revokeAccessType(login, accessType, getLogin());
    }

    @Override
    @RolesAllowed("getOwnAccesses")
    public AccessesDto getOwnAccesses() throws BaseException {
        Account account = accountManager.findByLogin(getLogin());
        return Mappers.getMapper(AccessMapper.class).toAccessesDto(account);
    }

    @Override
    @RolesAllowed("getOtherAccesses")
    public AccessesDto getOtherAccesses(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        return Mappers.getMapper(AccessMapper.class).toAccessesDto(account);
    }

    @Override
    public void switchAccessType(AccessType accessType) {
        log.info(String.format("User %s switched access type to %s.", getLogin(), accessType.toString()));
    }
}
