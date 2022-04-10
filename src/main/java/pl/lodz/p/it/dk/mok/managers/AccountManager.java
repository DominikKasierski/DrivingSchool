package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.ConfirmationCode;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.CodeType;
import pl.lodz.p.it.dk.exceptions.AccountException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.ConfirmationCodeException;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;
import pl.lodz.p.it.dk.mok.facades.ConfirmationCodeFacade;
import pl.lodz.p.it.dk.security.PasswordUtils;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Context
    ServletContext servletContext;

    @Inject
    private EmailService emailService;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private ConfirmationCodeFacade confirmationCodeFacade;


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
    public void confirmAccount(String code) throws BaseException {
        ConfirmationCode confirmationCode = confirmationCodeFacade.findByCode(code);
        Account account = confirmationCode.getAccount();

        if (account.isConfirmed()) {
            throw AccountException.alreadyActivated();
        } else if (confirmationCode.isUsed()) {
            throw ConfirmationCodeException.codeUsed();
        } else if (!confirmationCode.getCodeType().equals(CodeType.ACCOUNT_ACTIVATION)) {
            throw ConfirmationCodeException.wrongCodeType();
        }

        TraineeAccess traineeAccess = new TraineeAccess();
        traineeAccess.setAccount(account);
        traineeAccess.setCreatedBy(account);

        account.setConfirmed(true);
        account.getAccesses().add(traineeAccess);
        account.setConfirmModificationDate(Date.from(Instant.now()));
        account.setConfirmModificationBy(account);

        confirmationCode.setUsed(true);
        confirmationCode.setModificationDate(Date.from(Instant.now()));
        confirmationCode.setModifiedBy(account);

        accountFacade.edit(account);
        emailService.sendSuccessfulActivationEmail(account);
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

    @PermitAll
    public Account findByLogin(String login) throws BaseException {
        return accountFacade.findByLogin(login);
    }

    @RolesAllowed("lockAccount")
    public void lockAccount(Account account, Account adminAccount) throws BaseException {
        account.setEnabled(false);
        account.setEnableModificationDate(Date.from(Instant.now()));
        account.setEnableModificationBy(adminAccount);
        accountFacade.edit(account);
        emailService.sendAccountLockingEmail(account);
    }

    @RolesAllowed("unlockAccount")
    public void unlockAccount(Account account, Account adminAccount) throws BaseException {
        account.setEnabled(true);
        account.setEnableModificationDate(Date.from(Instant.now()));
        account.setEnableModificationBy(adminAccount);
        accountFacade.edit(account);
        emailService.sendAccountUnlockingEmail(account);
    }

    @RolesAllowed({"getOwnAccountDetails", "getOtherAccountDetails"})
    public Account getAccountDetails(String login) throws BaseException {
        return accountFacade.findByLogin(login);
    }

    @RolesAllowed("getAllAccounts")
    public List<Account> getAllAccounts() throws BaseException {
        return new ArrayList<>(accountFacade.findAll());
    }

    @RolesAllowed("editPersonalData")
    public void editPersonalData(Account account) throws BaseException {
        account.setModificationDate(Date.from(Instant.now()));
        account.setModifiedBy(account);
        accountFacade.edit(account);
    }

    @RolesAllowed({"editOwnEmail", "editOtherEmail"})
    public void editEmail(String login, String newEmail) throws BaseException {
        //TODO: Zaczac tutaj, dodac role do xmla
    }
}
