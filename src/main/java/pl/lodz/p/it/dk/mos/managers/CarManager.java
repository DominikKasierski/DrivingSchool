package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.facades.CarFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class CarManager {

    @Inject
    private AccountManager accountManager;

    @Inject
    private CarFacade carFacade;

    @RolesAllowed("addCar")
    public void addCar(Car car, String login) throws BaseException {
        car.setCreatedBy(accountManager.findByLogin(login));
        carFacade.create(car);
    }

}
