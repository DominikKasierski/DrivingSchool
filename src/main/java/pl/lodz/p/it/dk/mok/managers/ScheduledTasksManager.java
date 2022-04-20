package pl.lodz.p.it.dk.mok.managers;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.ConfirmationCode;
import pl.lodz.p.it.dk.entities.enums.CodeType;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;
import pl.lodz.p.it.dk.mok.facades.ConfirmationCodeFacade;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Log
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ScheduledTasksManager extends AbstractEndpoint {

    @Resource
    TimerService timerService;

    @Inject
    private ServletContext context;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private ConfirmationCodeFacade confirmationCodeFacade;

    @Inject
    private EmailService emailService;

    @PermitAll
    public void deleteUnverifiedAccounts(Timer time) {
        int expirationTime = Integer.parseInt(context.getInitParameter("codeExpirationTime"));
        Instant expirationInstant = Instant.now().minus(expirationTime, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationInstant);

        try {
            List<Account> unverifiedAccounts = accountFacade.findUnverifiedAccounts(expirationDate);
            for (Account account : unverifiedAccounts) {
                accountFacade.remove(account);
                emailService.sendAccountDeletingEmail(account);
            }
        } catch (BaseException e) {
            log.log(Level.WARNING, "Error during deleting unconfirmed accounts");
        }

        int halfOfExpirationTime = expirationTime / 2;
        Instant halfExpirationInstant = Instant.now().minus(halfOfExpirationTime, ChronoUnit.HOURS);
        Date halfExpirationDate = Date.from(halfExpirationInstant);

        try {
            List<ConfirmationCode> codes = confirmationCodeFacade.findCodesToResend(CodeType.ACCOUNT_ACTIVATION, halfExpirationDate);
            for (ConfirmationCode code : codes) {
                code.setSendAttempt(1);
                confirmationCodeFacade.edit(code);
                emailService.sendActivationEmail(code.getAccount(), code.getCode());
            }
        } catch (BaseException e) {
            log.log(Level.WARNING, "Error during resending codes for unconfirmed accounts");
        }
    }

}
