package pl.lodz.p.it.dk.mok.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;

@Local
public interface AccountEndpointLocal extends TransactionStarter {

    @PermitAll
    void registerAccount(RegisterAccountDto registerAccountDto) throws BaseException;

    @PermitAll
    void updateAuthInfo(String login, String language) throws BaseException;

    @PermitAll
    void updateAuthInfo(String login) throws BaseException;
}
