package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;

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

}
