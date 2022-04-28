package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.facades.TraineeAccessFacade;

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
public class TraineeAccessManager {

    @Inject
    private TraineeAccessFacade traineeAccessFacade;

    @PermitAll
    public TraineeAccess find(Account account) throws BaseException {
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.TRAINEE)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        return traineeAccessFacade.find(access.getId());
    }

    @RolesAllowed("createCourse")
    public void edit(TraineeAccess traineeAccess) throws BaseException {
        traineeAccessFacade.edit(traineeAccess);
    }
}
