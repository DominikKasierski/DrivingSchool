package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.*;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

@Local
public interface PaymentEndpointLocal extends TransactionStarter {

    @RolesAllowed("createPayment")
    public void createPayment(NewPaymentDto newPaymentDto) throws BaseException;

    @RolesAllowed("cancelPayment")
    public void cancelPayment() throws BaseException;

    @RolesAllowed("getPaymentsHistory")
    public List<PaymentDto> getPaymentsHistory(CourseCategory courseCategory) throws BaseException;

    @RolesAllowed("confirmPayment")
    public void confirmPayment(String login) throws BaseException;

    @RolesAllowed("rejectPayment")
    public void rejectPayment(RejectPaymentDto rejectPaymentDto) throws BaseException;

    @RolesAllowed("getPaymentsForApproval")
    public List<PaymentForApprovalDto> getPaymentsForApproval() throws BaseException;

    @RolesAllowed("getUnderpayments")
    public List<UnderpaymentDto> getUnderpayments(CourseCategory courseCategory) throws BaseException;

    @RolesAllowed("addPayment")
    public void addPayment(String login, NewPaymentDto newPaymentDto) throws BaseException;
}
