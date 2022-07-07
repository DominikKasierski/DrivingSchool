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
import pl.lodz.p.it.dk.mos.dtos.*;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.PaymentManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.Date;
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
    public List<PaymentDto> getPaymentsHistory() throws BaseException {
        List<Payment> paymentsForGivenCategory = paymentManager.getPayments(getLogin());
        List<PaymentDto> paymentsDto = new ArrayList<>();

        for (Payment payment : paymentsForGivenCategory) {
            paymentsDto.add(Mappers.getMapper(PaymentMapper.class).toPaymentDto(payment));
        }

        return paymentsDto;
    }

    @Override
    @RolesAllowed("confirmPayment")
    public void confirmPayment(ConfirmPaymentDto confirmPaymentDto) throws BaseException {
        Course course = courseManager.getOngoingCourse(confirmPaymentDto.getLogin());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.confirmPayment(confirmPaymentDto.getAdminComment(), course, getLogin());
    }

    @Override
    @RolesAllowed("rejectPayment")
    public void rejectPayment(RejectPaymentDto rejectPaymentDto) throws BaseException {
        Course course = courseManager.getOngoingCourse(rejectPaymentDto.getLogin());
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.rejectPayment(rejectPaymentDto.getAdminComment(), course, getLogin());
    }

    @Override
    @RolesAllowed("getPaymentsForApproval")
    public List<PaymentForApprovalDto> getPaymentsForApproval() throws BaseException {
        List<Payment> inProgressPayments = paymentManager.getInProgressPayments();
        List<PaymentForApprovalDto> paymentsForApproval = new ArrayList<>();

        for (Payment payment : inProgressPayments) {
            paymentsForApproval.add(Mappers.getMapper(PaymentMapper.class).toPaymentForApprovalDto(payment));
        }

        return paymentsForApproval;
    }

    @Override
    @RolesAllowed("getUnderpayments")
    public List<UnderpaymentDto> getUnderpayments(CourseCategory courseCategory) throws BaseException {
        return paymentManager.getUnderpayments(courseCategory);
    }

    @Override
    @RolesAllowed("addPayment")
    public void addPayment(String login, NewPaymentDto newPaymentDto) throws BaseException {
        Course course = courseManager.getOngoingCourse(login);
        CourseDto courseDto = Mappers.getMapper(CourseMapper.class).toCourseDto(course);
        verifyEntityIntegrity(courseDto);
        paymentManager.addPayment(newPaymentDto, course, getLogin());
    }

    @Override
    @RolesAllowed("generateReport")
    public GenerateReportDto generateReport(Long from, Long to) throws BaseException {
        List<ReportRowDto> reportContent = new ArrayList<>();
        paymentManager.generateReport(new Date(from * 1000), new Date(to * 1000))
                .forEach(x -> reportContent.add(Mappers.getMapper(PaymentMapper.class).toReportRowDto(x)));

        return new GenerateReportDto(reportContent, reportContent.size());
    }
}
