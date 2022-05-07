package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CarDto;
import pl.lodz.p.it.dk.mos.dtos.EditCarDto;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface CarEndpointLocal extends TransactionStarter {

    @RolesAllowed("addCar")
    void addCar(NewCarDto newCarDto) throws BaseException;

    @RolesAllowed("removeCar")
    void removeCar(Long id) throws BaseException;

    @RolesAllowed("editCar")
    public void editCar(EditCarDto editCarDto) throws BaseException;

    @RolesAllowed("getCar")
    CarDto getCar(Long id) throws BaseException;
}
