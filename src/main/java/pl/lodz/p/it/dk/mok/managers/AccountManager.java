package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.time.Instant;
import java.util.Date;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Context
    ServletContext servletContext;

    @Inject
    private AccountFacade accountFacade;

    private static int FAILED_LOGIN_ATTEMPTS_LIMIT;

    @PostConstruct
    private void init() {
        FAILED_LOGIN_ATTEMPTS_LIMIT = Integer.parseInt(servletContext.getInitParameter("failedLoginAttemptsLimit"));
    }

    @PermitAll
    public void updateAuthInfo(String login, String language) throws BaseException {
        Account account = accountFacade.findByLogin(login);
        account.setFailedLoginAttempts(0);
        account.setLanguage(language);
        accountFacade.edit(account);
    }

    @PermitAll
    public void updateAuthInfo(String login) throws BaseException {
        Account account = accountFacade.findByLogin(login);
        int failedLoginAttempts = account.getFailedLoginAttempts() + 1;

        if (failedLoginAttempts == FAILED_LOGIN_ATTEMPTS_LIMIT) {
            account.setEnabled(false);
            account.setEnableModificationDate(Date.from(Instant.now()));
            account.setEnableModificationBy(null);
            //TODO: WYSŁAĆ EMAIL!
        }

        account.setFailedLoginAttempts(failedLoginAttempts);
        accountFacade.edit(account);
    }
}
