package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.endpoints.PaymentEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/payment")
public class PaymentController extends AbstractController {

    @Inject
    private PaymentEndpointLocal paymentEndpoint;

    @POST
    @EtagFilterBinding
    @RolesAllowed("createPayment")
    @Path("/createPayment")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPayment(@NotNull @Valid NewPaymentDto newPaymentDto) throws BaseException {
        repeat(() -> paymentEndpoint.createPayment(newPaymentDto), paymentEndpoint);
    }
}
