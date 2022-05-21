package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CarMapper;
import pl.lodz.p.it.dk.mos.dtos.BriefCarInfoDto;
import pl.lodz.p.it.dk.mos.dtos.CarDto;
import pl.lodz.p.it.dk.mos.dtos.EditCarDto;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;
import pl.lodz.p.it.dk.mos.managers.CarManager;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CarEndpoint extends AbstractEndpoint implements CarEndpointLocal {

    @Inject
    CarManager carManager;

    @Override
    @RolesAllowed("addCar")
    public void addCar(NewCarDto newCarDto) throws BaseException {
        Car car = new Car();
        Mappers.getMapper(CarMapper.class).toCar(newCarDto, car);
        carManager.addCar(car, getLogin());
    }

    @Override
    @RolesAllowed("removeCar")
    public void removeCar(Long id) throws BaseException {
        Car car = carManager.find(id);
        CarDto carDto = Mappers.getMapper(CarMapper.class).toCarDto(car);
        verifyEntityIntegrity(carDto);
        carManager.removeCar(car, getLogin());
    }

    @Override
    @RolesAllowed("editCar")
    public void editCar(EditCarDto editCarDto) throws BaseException {
        Car car = carManager.find(editCarDto.getId());
        CarDto carDto = Mappers.getMapper(CarMapper.class).toCarDto(car);
        verifyEntityIntegrity(carDto);
        Mappers.getMapper(CarMapper.class).toCar(editCarDto, car);
        carManager.editCar(car, getLogin());
    }

    @Override
    @RolesAllowed("getCar")
    public CarDto getCar(Long id) throws BaseException {
        Car car = carManager.find(id);
        return Mappers.getMapper(CarMapper.class).toCarDto(car);
    }

    @Override
    @PermitAll
    public List<BriefCarInfoDto> getAllCars() throws BaseException {
        List<Car> cars = carManager.findAllUndeletedCars();
        List<BriefCarInfoDto> briefCarsInfoDto = new ArrayList<>();

        for (Car car : cars) {
            briefCarsInfoDto.add(Mappers.getMapper(CarMapper.class).toBriefCarInfoDto(car));
        }

        return briefCarsInfoDto;
    }

}
