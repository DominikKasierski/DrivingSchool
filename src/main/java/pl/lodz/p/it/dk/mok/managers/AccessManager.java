package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.AccountException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailService emailService;

    @RolesAllowed("grantAccessType")
    public void grantAccessType(String login, AccessType accessType, String adminLogin) throws BaseException {
        Account account = accountFacade.findByLogin(login);

        if (!account.isConfirmed()) {
            throw AccountException.accountUnconfirmed();
        } else if (!account.isEnabled()) {
            throw AccountException.accountBlocked();
        }

        Set<AccessType> accessesGranted = account.getAccesses()
                .stream()
                .filter(Access::isActivated)
                .map(Access::getAccessType)
                .collect(Collectors.toSet());

        if (accessType.equals(AccessType.TRAINEE) && (accessesGranted.contains(AccessType.INSTRUCTOR)) ||
                (accessesGranted.contains(AccessType.ADMIN))) {
            throw AccessException.unsupportedCombination();
        } else if ((accessType.equals(AccessType.INSTRUCTOR) || accessType.equals(AccessType.ADMIN)) &&
                accessesGranted.contains(AccessType.TRAINEE)) {
            throw AccessException.unsupportedCombination();
        }

        Account adminAccount = accountFacade.findByLogin(adminLogin);
        Optional<Access> access = account.getAccesses().stream()
                .filter(x -> x.getAccessType().equals(accessType))
                .findAny();

        if (access.isPresent() && access.get().isActivated()) {
            throw AccessException.alreadyGranted();
        } else if (access.isPresent() && !access.get().isActivated()) {
            access.get().setActivated(true);
            access.get().setModificationDate(Date.from(Instant.now()));
            access.get().setModifiedBy(adminAccount);
        } else {
            Access newAccess = createAccess(accessType);
            newAccess.setAccount(account);
            newAccess.setCreatedBy(adminAccount);
            account.getAccesses().add(newAccess);
        }

        account.setAccesses(new HashSet<>(account.getAccesses()));

        accountFacade.edit(account);
        emailService.sendAccessGrantingEmail(account, accessType.toString());
    }

    @RolesAllowed("revokeAccessType")
    public void revokeAccessType(String login, AccessType accessType, String adminLogin) throws BaseException {
        Account account = accountFacade.findByLogin(login);

        if (!account.isConfirmed()) {
            throw AccountException.accountUnconfirmed();
        } else if (!account.isEnabled()) {
            throw AccountException.accountBlocked();
        }

        Account adminAccount = accountFacade.findByLogin(adminLogin);
        Optional<Access> access = account.getAccesses().stream()
                .filter(x -> x.getAccessType().equals(accessType))
                .findAny();

        if (access.isEmpty() || !access.get().isActivated()) {
            throw AccessException.alreadyRevoked();
        }

        access.get().setActivated(false);
        access.get().setModificationDate(Date.from(Instant.now()));
        access.get().setModifiedBy(adminAccount);
        account.setAccesses(new HashSet<>(account.getAccesses()));

        accountFacade.edit(account);
        emailService.sendAccessRevokingEmail(account, accessType.toString());
    }

    private Access createAccess(AccessType accessType) throws AccessException {
        switch (accessType) {
            case TRAINEE:
                return new TraineeAccess();
            case INSTRUCTOR:
                return new InstructorAccess();
            case ADMIN:
                return new AdminAccess();
        }
        throw AccessException.unsupportedAccessType();
    }
}
