package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;

@Mapper
public interface CarMapper {

    void toCar(NewCarDto newCarDto, @MappingTarget Car car);

}
