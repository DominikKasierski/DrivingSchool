package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.ConfirmationCode;
import pl.lodz.p.it.dk.entities.enums.CodeType;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;
import pl.lodz.p.it.dk.security.PasswordUtils;

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
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Context
    ServletContext servletContext;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailService emailService;

    private static int FAILED_LOGIN_ATTEMPTS_LIMIT;

    @PostConstruct
    private void init() {
        FAILED_LOGIN_ATTEMPTS_LIMIT = Integer.parseInt(servletContext.getInitParameter("failedLoginAttemptsLimit"));
    }

    @PermitAll
    public void registerAccount(Account account, String language) throws BaseException {
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setCode(UUID.randomUUID().toString());
        confirmationCode.setUsed(false);
        confirmationCode.setAccount(account);
        confirmationCode.setCodeType(CodeType.ACCOUNT_ACTIVATION);
        confirmationCode.setCreatedBy(account);

        account.setEnabled(true);
        account.setConfirmed(false);
        account.setPassword(PasswordUtils.generate(account.getPassword()));
        account.setLanguage(language);
        account.setCreatedBy(account);
        account.getConfirmationCodes().add(confirmationCode);

        accountFacade.create(account);
        emailService.sendActivationEmail(account, confirmationCode.getCode());
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
            emailService.sendAccountLockingEmail(account);
        }

        account.setFailedLoginAttempts(failedLoginAttempts);
        accountFacade.edit(account);
    }
}
