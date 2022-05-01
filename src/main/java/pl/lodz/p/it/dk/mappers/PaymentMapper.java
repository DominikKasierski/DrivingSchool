package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;

@Mapper
public interface PaymentMapper {

    @Mapping(target = "courseId", source = "course.id")
    PaymentDto toPaymentDto(Payment payment);
}
