package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.entities.enums.PaymentStatus;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.PaymentException;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.facades.PaymentFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PaymentManager {

    @Inject
    CourseManager courseManager;

    @Inject
    AccountManager accountManager;

    @Inject
    PaymentFacade paymentFacade;

    @RolesAllowed("createPayment")
    public void createPayment(NewPaymentDto newPaymentDto, Course course, String login) throws BaseException {
        boolean hasPaymentInProgress = course.getPayments().stream()
                .anyMatch(x -> x.getPaymentStatus().equals(PaymentStatus.IN_PROGRESS));

        if (hasPaymentInProgress) {
            throw PaymentException.paymentInProgress();
        }

        List<Payment> payments = paymentFacade.findByCourseId(course.getId());
        BigDecimal newValue = new BigDecimal(String.valueOf(newPaymentDto.getValue()));
        BigDecimal totalValue = payments.stream()
                .map(Payment::getValue)
                .reduce(newValue, BigDecimal::add);

        if (totalValue.compareTo(course.getCourseDetails().getPrice()) > 0) {
            throw PaymentException.courseOverpaid();
        }

        Account account = accountManager.findByLogin(login);
        Payment payment = new Payment(course, newPaymentDto.getValue(), newPaymentDto.getTraineeComment());
        payment.setCreatedBy(account);

        course.getPayments().add(payment);
        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(account);
        courseManager.edit(course);
    }

    @RolesAllowed("cancelPayment")
    public void cancelPayment(Course course) throws BaseException {
        Payment payment = course.getPayments().stream()
                .filter(x -> x.getPaymentStatus().equals(PaymentStatus.IN_PROGRESS))
                .findAny()
                .orElseThrow(PaymentException::noPaymentInProgress);

        Account account = payment.getCreatedBy();
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setModificationDate(Date.from(Instant.now()));
        payment.setModifiedBy(account);

        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(account);
        courseManager.edit(course);
    }

    @RolesAllowed("getPaymentsHistory")
    public List<Payment> getPayments(String login, CourseCategory courseCategory) throws BaseException {
        Course course = courseManager.getOngoingCourse(login);
        List<Payment> payments = paymentFacade.findByCourseId(course.getId());
        payments.sort(Comparator.comparing(Payment::getCreationDate));
        return payments;
    }
}
