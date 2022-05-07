package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.entities.DrivingLesson;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.CarException;
import pl.lodz.p.it.dk.mos.facades.CarFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.util.Date;

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

    @RolesAllowed("removeCar")
    public void removeCar(Car car, String login) throws BaseException {
        car.getDrivingLessons().stream()
                .map(DrivingLesson::getLessonStatus)
                .filter(x -> x.equals(LessonStatus.IN_PROGRESS) || x.equals(LessonStatus.PENDING))
                .findAny()
                .orElseThrow(CarException::carInUse);

        car.setDeleted(true);
        car.setModificationDate(Date.from(Instant.now()));
        car.setModifiedBy(accountManager.findByLogin(login));
        carFacade.edit(car);
    }

    @RolesAllowed("editCar")
    public void editCar(Car car, String login) throws BaseException {
        car.setModificationDate(Date.from(Instant.now()));
        car.setModifiedBy(accountManager.findByLogin(login));
        carFacade.edit(car);
    }

    @RolesAllowed({"editCar", "removeCar", "getCar"})
    public Car find(Object id) throws BaseException {
        return carFacade.find(id);
    }

}
