package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface CourseMapper {

    @Mappings({
            @Mapping(target = "accountId", source = "trainee.account.id"),
            @Mapping(target = "courseDetailsId", source = "courseDetails.id"),
            @Mapping(target = "lectureGroupId", source = "lectureGroup.id")
    })
    CourseDto toCourseDto(Course course);

    @Named("mapPayments")
    default Set<PaymentDto> mapPayments(Set<Payment> payments) {
        Set<PaymentDto> paymentsDto = new HashSet<>();
        payments.forEach(x -> paymentsDto.add(Mappers.getMapper(PaymentMapper.class).toPaymentDto(x)));
        return paymentsDto;
    }
}