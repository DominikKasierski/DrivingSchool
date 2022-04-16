package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.common.configs.AppConfig;
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
import javax.security.enterprise.SecurityContext;
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

    @Inject
    private SecurityContext securityContext;

    @Inject
    private AppConfig appConfig;


    private static int FAILED_LOGIN_ATTEMPTS_LIMIT;
    private static long PASSWORD_RESET_SECONDS_LIMIT;

    @PostConstruct
    private void init() {
        FAILED_LOGIN_ATTEMPTS_LIMIT = Integer.parseInt(servletContext.getInitParameter("failedLoginAttemptsLimit"));
        PASSWORD_RESET_SECONDS_LIMIT = appConfig.getPasswordResetSecondsLimit();
    }

    @PermitAll
    public void registerAccount(Account account, String language) throws BaseException {
        ConfirmationCode confirmationCode =
                new ConfirmationCode(UUID.randomUUID().toString(), account, CodeType.ACCOUNT_ACTIVATION, account);

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

        accountFacade.edit(account);
        account.setFailedLoginAttempts(failedLoginAttempts);
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
        if (accountFacade.checkEmailOccurrence(newEmail)) {
            throw AccountException.emailExists();
        }

        Account account = accountFacade.findByLogin(login);
        Account changingAccount = accountFacade.findByLogin(securityContext.getCallerPrincipal().getName());
        deactivatePreviousCodes(account, CodeType.EMAIL_CHANGE, changingAccount);
        ConfirmationCode confirmationCode =
                new ConfirmationCode(UUID.randomUUID().toString(), account, CodeType.EMAIL_CHANGE, changingAccount);

        account.setNewEmailAddress(newEmail);
        account.getConfirmationCodes().add(confirmationCode);
        account.setEmailModificationDate(Date.from(Instant.now()));
        account.setEmailModificationBy(changingAccount);

        accountFacade.edit(account);
        emailService.sendEmailChangingEmail(account, confirmationCode.getCode());
    }

    @PermitAll
    public void confirmEmail(String code) throws BaseException {
        ConfirmationCode confirmationCode = confirmationCodeFacade.findByCode(code);

        if (confirmationCode.isUsed()) {
            throw ConfirmationCodeException.codeUsed();
        } else if (!confirmationCode.getCodeType().equals(CodeType.EMAIL_CHANGE)) {
            throw ConfirmationCodeException.wrongCodeType();
        }

        Account account = confirmationCode.getAccount();
        account.setEmailAddress(account.getNewEmailAddress());
        account.setNewEmailAddress(null);
        account.setEmailModificationDate(Date.from(Instant.now()));
        account.setEmailModificationBy(account);

        confirmationCode.setUsed(true);
        confirmationCode.setModificationDate(Date.from(Instant.now()));
        confirmationCode.setModifiedBy(account);

        accountFacade.edit(account);
    }

    @RolesAllowed("changePassword")
    public void changePassword(Account account, String oldPassword, String newPassword) throws BaseException {
        if (!PasswordUtils.verify(oldPassword, account.getPassword())) {
            throw AccountException.wrongPassword();
        } else if (PasswordUtils.verify(newPassword, account.getPassword())) {
            throw AccountException.samePassword();
        }

        account.setPassword(PasswordUtils.generate(newPassword));
        account.setPasswordModificationDate(Date.from(Instant.now()));
        account.setPasswordModificationBy(account);

        accountFacade.edit(account);
    }

    @PermitAll
    public void resetPassword(String email) throws BaseException {
        Account account = accountFacade.findByEmail(email);

        if (!account.isConfirmed()) {
            throw AccountException.accountUnconfirmed();
        } else if (!account.isEnabled()) {
            throw AccountException.accountBlocked();
        }

        deactivatePreviousCodes(account, CodeType.PASSWORD_RESET, account);
        ConfirmationCode confirmationCode =
                new ConfirmationCode(UUID.randomUUID().toString(), account, CodeType.PASSWORD_RESET, account);
        account.getConfirmationCodes().add(confirmationCode);

        accountFacade.edit(account);
        emailService.sendPasswordResetEmail(account, confirmationCode.getCode());
    }

    @PermitAll
    public void confirmPasswordChange(String code, String newPassword) throws BaseException {
        ConfirmationCode confirmationCode = confirmationCodeFacade.findByCode(code);
        Account account = accountFacade.findByLogin(confirmationCode.getAccount().getLogin());

        if (!account.isConfirmed()) {
            throw AccountException.accountUnconfirmed();
        } else if (!account.isEnabled()) {
            throw AccountException.accountBlocked();
        } else if (confirmationCode.isUsed()) {
            throw ConfirmationCodeException.codeUsed();
        } else if (!confirmationCode.getCodeType().equals(CodeType.PASSWORD_RESET)) {
            throw ConfirmationCodeException.wrongCodeType();
        }

        Date codeExpirationDate = new Date(confirmationCode.getCreationDate().getTime() + PASSWORD_RESET_SECONDS_LIMIT);
        Date currentDate = Date.from(Instant.now());

        if (currentDate.after(codeExpirationDate)) {
            throw ConfirmationCodeException.codeExpired();
        }

        confirmationCode.setUsed(true);
        confirmationCode.setModificationDate(Date.from(Instant.now()));
        confirmationCode.setModifiedBy(account);

        if (PasswordUtils.verify(newPassword, account.getPassword())) {
            throw AccountException.samePassword();
        }

        account.setPassword(PasswordUtils.generate(newPassword));
        account.setPasswordModificationDate(Date.from(Instant.now()));
        account.setPasswordModificationBy(account);

        accountFacade.edit(account);
    }

    private void deactivatePreviousCodes(Account account, CodeType codeType, Account changingAccount) {
        for (ConfirmationCode confirmationCode : account.getConfirmationCodes()) {
            if (confirmationCode.getCodeType().equals(codeType) && !confirmationCode.isUsed()) {
                confirmationCode.setUsed(true);
                confirmationCode.setModificationDate(Date.from(Instant.now()));
                confirmationCode.setModifiedBy(changingAccount);
            }
        }
    }

}
