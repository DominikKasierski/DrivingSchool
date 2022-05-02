package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;

@Mapper
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);
}
