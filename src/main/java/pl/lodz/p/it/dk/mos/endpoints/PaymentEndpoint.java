package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.PaymentManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

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
        Course course = courseManager.findById(newPaymentDto.getCourseId());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.createPayment(newPaymentDto, course, getLogin());
    }
}
