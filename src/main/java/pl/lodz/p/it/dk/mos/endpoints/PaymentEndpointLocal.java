package pl.lodz.p.it.dk.mos.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface PaymentEndpointLocal extends TransactionStarter {

    @RolesAllowed("createPayment")
    public void createPayment(NewPaymentDto newPaymentDto) throws BaseException;
}
