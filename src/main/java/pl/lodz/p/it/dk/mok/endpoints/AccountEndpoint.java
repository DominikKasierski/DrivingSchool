package pl.lodz.p.it.dk.mok.endpoints;

import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.AccountMapper;
import pl.lodz.p.it.dk.mok.dtos.AccountDto;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;
import pl.lodz.p.it.dk.mok.managers.AccountManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Log
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint extends AbstractEndpoint implements AccountEndpointLocal {

    @Inject
    private AccountManager accountManager;

    @Inject
    private HttpServletRequest servletRequest;

    @Override
    public void registerAccount(RegisterAccountDto registerAccountDto) throws BaseException {
        Account account = new Account();
        Mappers.getMapper(AccountMapper.class).toAccount(registerAccountDto, account);
        String language = servletRequest.getLocale().getLanguage().toLowerCase();
        accountManager.registerAccount(account, language);
    }

    @Override
    @PermitAll
    public void confirmAccount(String code) throws BaseException {
        accountManager.confirmAccount(code);
    }

    @Override
    @PermitAll
    public void updateAuthInfo(String login, String language) throws BaseException {
        accountManager.updateAuthInfo(login, language);
    }

    @Override
    @PermitAll
    public void updateAuthInfo(String login) throws BaseException {
        accountManager.updateAuthInfo(login);
    }

    @Override
    @RolesAllowed("lockAccount")
    public void lockAccount(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        AccountDto accountDto = Mappers.getMapper(AccountMapper.class).toAccountDto(account);
        verifyEntityIntegrity(accountDto);
        Account adminAccount = accountManager.findByLogin(getLogin());
        accountManager.lockAccount(account, adminAccount);
    }

    @Override
    @RolesAllowed("unlockAccount")
    public void unlockAccount(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        AccountDto accountDto = Mappers.getMapper(AccountMapper.class).toAccountDto(account);
        verifyEntityIntegrity(accountDto);
        Account adminAccount = accountManager.findByLogin(getLogin());
        accountManager.unlockAccount(account, adminAccount);
    }
}
