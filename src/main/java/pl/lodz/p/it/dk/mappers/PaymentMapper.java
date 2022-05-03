package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;
import pl.lodz.p.it.dk.mos.dtos.PaymentsForApprovalDto;

@Mapper
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);

    @Mappings({
            @Mapping(target = "firstname", source = "createdBy.firstname"),
            @Mapping(target = "lastname", source = "createdBy.lastname"),
            @Mapping(target = "courseCategory", source = "course.courseDetails.courseCategory"),
    })
    PaymentsForApprovalDto toPaymentsForApprovalDto(Payment payment);
}

