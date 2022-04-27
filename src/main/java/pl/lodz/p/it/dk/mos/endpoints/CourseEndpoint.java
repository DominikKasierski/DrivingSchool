package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.AccessMapper;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;
import pl.lodz.p.it.dk.mos.managers.AccessManager;
import pl.lodz.p.it.dk.mos.managers.AccountManager;
import pl.lodz.p.it.dk.mos.managers.CourseManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CourseEndpoint extends AbstractEndpoint implements CourseEndpointLocal {

    @Inject
    AccountManager accountManager;

    @Inject
    AccessManager accessManager;

    @Inject
    CourseManager courseManager;

    @Override
    @RolesAllowed("createCourse")
    public void createCourse(CourseCategory courseCategory) throws BaseException {
        Account account = accountManager.findByLogin(getLogin());
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.TRAINEE)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        TraineeAccess traineeAccess = accessManager.find(access.getId());
        TraineeAccessDto traineeAccessDto = Mappers.getMapper(AccessMapper.class).toTraineeAccessDto(traineeAccess);
        verifyEntityIntegrity(traineeAccessDto);

        courseManager.createCourse(courseCategory, traineeAccess);
    }

}
