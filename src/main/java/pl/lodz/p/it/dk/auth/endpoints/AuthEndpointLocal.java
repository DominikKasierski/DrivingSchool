package pl.lodz.p.it.dk.auth.endpoints;

import pl.lodz.p.it.dk.auth.dtos.LoginDataDto;
import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface AuthEndpointLocal extends TransactionStarter {

    @PermitAll
    String login(LoginDataDto loginDataDto) throws BaseException;

    @RolesAllowed("logout")
    void logout();

    @RolesAllowed("updateToken")
    public String updateToken(String currentToken);
}
