package pl.lodz.p.it.dk.mok.managers;

import pl.lodz.p.it.dk.common.email.EmailService;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.AccountException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.InstructorAccessException;
import pl.lodz.p.it.dk.mok.dtos.InstructorAccessDto;
import pl.lodz.p.it.dk.mok.facades.AccountFacade;
import pl.lodz.p.it.dk.mok.facades.InstructorAccessFacade;
import pl.lodz.p.it.dk.mok.facades.TraineeAccessFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class AccessManager {

    @Inject
    private AccountManager accountManager;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private TraineeAccessFacade traineeAccessFacade;

    @Inject
    private InstructorAccessFacade instructorAccessFacade;

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

        if (accessType.equals(AccessType.TRAINEE) && ((accessesGranted.contains(AccessType.INSTRUCTOR)) ||
                (accessesGranted.contains(AccessType.ADMIN)))) {
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

    @RolesAllowed("getTraineeAccess")
    public TraineeAccess findTraineeAccess(Account account) throws BaseException {
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.TRAINEE)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        return traineeAccessFacade.find(access.getId());
    }

    @RolesAllowed({"getAllInstructors", "getInstructorAccess", "addInstructorPermission", "removeInstructorPermission"})
    public InstructorAccess findInstructorAccess(Account account) throws BaseException {
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.INSTRUCTOR)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        return instructorAccessFacade.find(access.getId());
    }

    @RolesAllowed("getAllInstructors")
    public List<InstructorAccessDto> getAllInstructors() throws BaseException {
        List<Account> accounts = accountManager.getAllAccounts();
        List<InstructorAccessDto> instructors = new ArrayList<>();

        for (Account account : accounts) {
            boolean isInstructor = account.getAccesses().stream()
                    .anyMatch(x -> x.getAccessType() == AccessType.INSTRUCTOR && x.isActivated());
            if (isInstructor) {
                InstructorAccess instructorAccess = findInstructorAccess(account);
                String permissions = instructorAccess.getPermissions().stream()
                        .map(Enum::toString)
                        .collect(Collectors.joining(", "));
                instructors
                        .add(new InstructorAccessDto(account.getLogin(), account.getFirstname(), account.getLastname(),
                                permissions, instructorAccess.getVersion()));
            }
        }

        return instructors;
    }

    @RolesAllowed({"getInstructorAccess", "addInstructorPermission", "removeInstructorPermission"})
    public InstructorAccessDto getInstructorAccess(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        InstructorAccess instructorAccess = findInstructorAccess(account);
        String permissions = instructorAccess.getPermissions().stream()
                .map(Enum::toString)
                .collect(Collectors.joining(", "));
        return new InstructorAccessDto(account.getLogin(), account.getFirstname(), account.getLastname(), permissions,
                instructorAccess.getVersion());
    }

    @RolesAllowed("getOwnPermissions")
    public String getOwnPermissions(Account account) throws BaseException {
        InstructorAccess instructorAccess = findInstructorAccess(account);
        return instructorAccess.getPermissions().stream()
                .map(Enum::toString)
                .collect(Collectors.joining(", "));
    }

    @RolesAllowed("addInstructorPermission")
    public void addInstructorPermission(Account account, CourseCategory courseCategory, Account adminAccount)
            throws BaseException {
        InstructorAccess instructorAccess = findInstructorAccess(account);

        if (instructorAccess.getPermissions().contains(courseCategory)) {
            throw InstructorAccessException.permissionAlreadyAdded();
        }

        instructorAccess.getPermissions().add(courseCategory);
        instructorAccess.setModificationDate(Date.from(Instant.now()));
        instructorAccess.setModifiedBy(adminAccount);
        instructorAccessFacade.edit(instructorAccess);
    }

    @RolesAllowed("removeInstructorPermission")
    public void removeInstructorPermission(Account account, CourseCategory courseCategory, Account adminAccount)
            throws BaseException {
        InstructorAccess instructorAccess = findInstructorAccess(account);

        if (!instructorAccess.getPermissions().contains(courseCategory)) {
            throw InstructorAccessException.permissionAlreadyRemoved();
        }

        instructorAccess.getPermissions().remove(courseCategory);
        instructorAccess.setModificationDate(Date.from(Instant.now()));
        instructorAccess.setModifiedBy(adminAccount);
        instructorAccessFacade.edit(instructorAccess);
    }

    private Access createAccess(AccessType accessType) throws BaseException {
        switch (accessType) {
            case TRAINEE:
                return new TraineeAccess();
            case INSTRUCTOR:
                Set<CourseCategory> permissions = new HashSet<>(Collections.singletonList(CourseCategory.B));
                return new InstructorAccess(permissions);
            case ADMIN:
                return new AdminAccess();
        }
        throw AccessException.unsupportedAccessType();
    }
}
