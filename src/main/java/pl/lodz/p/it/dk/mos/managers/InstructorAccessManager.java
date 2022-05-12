package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.InstructorAccess;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.facades.InstructorAccessFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class InstructorAccessManager {

    @Inject
    private InstructorAccessFacade instructorAccessFacade;

    @PermitAll
    public InstructorAccess find(Account account) throws BaseException {
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.INSTRUCTOR)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        return instructorAccessFacade.find(access.getId());
    }

    @RolesAllowed("addLectureForGroup")
    public void edit(InstructorAccess instructorAccess) throws BaseException {
        instructorAccessFacade.edit(instructorAccess);
    }
}
