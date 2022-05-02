package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mappers.PaymentMapper;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.PaymentManager;

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
public class PaymentEndpoint extends AbstractEndpoint implements PaymentEndpointLocal {

    @Inject
    CourseManager courseManager;

    @Inject
    PaymentManager paymentManager;

    @Override
    @RolesAllowed("createPayment")
    public void createPayment(NewPaymentDto newPaymentDto) throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.createPayment(newPaymentDto, course, getLogin());
    }

    @Override
    @RolesAllowed("cancelPayment")
    public void cancelPayment() throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.cancelPayment(course);
    }

    @Override
    @RolesAllowed("getPaymentsHistory")
    public List<PaymentDto> getPaymentsHistory(CourseCategory courseCategory) throws BaseException {
        List<Payment> paymentsForGivenCategory = paymentManager.getPayments(getLogin(), courseCategory);
        List<PaymentDto> paymentsDto = new ArrayList<>();

        for (Payment payment : paymentsForGivenCategory) {
            paymentsDto.add(Mappers.getMapper(PaymentMapper.class).toPaymentDto(payment));
        }

        return paymentsDto;
    }

    @Override
    @RolesAllowed("confirmPayment")
    public void confirmPayment(String login) throws BaseException {
        Course course = courseManager.getOngoingCourse(login);
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.confirmPayment(course, getLogin());
    }
}
