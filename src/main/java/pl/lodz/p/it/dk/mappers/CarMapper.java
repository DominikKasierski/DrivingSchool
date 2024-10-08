package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.mos.dtos.BriefCarInfoDto;
import pl.lodz.p.it.dk.mos.dtos.CarDto;
import pl.lodz.p.it.dk.mos.dtos.EditCarDto;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;

@Mapper
public interface CarMapper {

    void toCar(NewCarDto newCarDto, @MappingTarget Car car);

    void toCar(EditCarDto editCarDto, @MappingTarget Car car);

    CarDto toCarDto(Car car);

    BriefCarInfoDto toBriefCarInfoDto(Car car);
}