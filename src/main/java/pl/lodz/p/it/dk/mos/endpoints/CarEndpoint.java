package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CarMapper;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;
import pl.lodz.p.it.dk.mos.managers.CarManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CarEndpoint extends AbstractEndpoint implements CarEndpointLocal {

    @Inject
    CarManager carManager;

    @RolesAllowed("addCar")
    public void addCar(NewCarDto newCarDto) throws BaseException {
        Car car = new Car();
        Mappers.getMapper(CarMapper.class).toCar(newCarDto, car);
        carManager.addCar(car, getLogin());
    }

}
