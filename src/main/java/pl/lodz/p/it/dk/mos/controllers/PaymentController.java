package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewPaymentDto;
import pl.lodz.p.it.dk.mos.dtos.PaymentDto;
import pl.lodz.p.it.dk.mos.endpoints.PaymentEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    @PUT
    @EtagFilterBinding
    @RolesAllowed("cancelPayment")
    @Path("/cancelPayment")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cancelPayment() throws BaseException {
        repeat(() -> paymentEndpoint.cancelPayment(), paymentEndpoint);
    }

    @GET
    @RolesAllowed("getPaymentsHistory")
    @Path("/getPaymentsHistory/{courseCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentDto> getPaymentsHistory(@NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        return repeat(() -> paymentEndpoint.getPaymentsHistory(courseCategory), paymentEndpoint);
    }
}
