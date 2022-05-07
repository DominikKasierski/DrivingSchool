package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.*;
import pl.lodz.p.it.dk.mos.endpoints.PaymentEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.validation.annotations.Login;

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

    @GET
    @RolesAllowed("getPaymentsForApproval")
    @Path("/getPaymentsForApproval")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentForApprovalDto> getPaymentsForApproval() throws BaseException {
        return repeat(() -> paymentEndpoint.getPaymentsForApproval(), paymentEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("confirmPayment")
    @Path("/confirmPayment/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void confirmPayment(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        repeat(() -> paymentEndpoint.confirmPayment(login), paymentEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("rejectPayment")
    @Path("/rejectPayment")
    @Consumes(MediaType.APPLICATION_JSON)
    public void rejectPayment(@NotNull @Valid RejectPaymentDto rejectPaymentDto) throws BaseException {
        repeat(() -> paymentEndpoint.rejectPayment(rejectPaymentDto), paymentEndpoint);
    }

    @GET
    @RolesAllowed("getUnderpayments")
    @Path("/getUnderpayments/{courseCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UnderpaymentDto> getUnderpayments(@NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        return repeat(() -> paymentEndpoint.getUnderpayments(courseCategory), paymentEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("addPayment")
    @Path("/addPayment/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addPayment(@NotNull @Login @PathParam("login") @Valid String login,
                           @NotNull @Valid NewPaymentDto newPaymentDto) throws BaseException {
        repeat(() -> paymentEndpoint.addPayment(login, newPaymentDto), paymentEndpoint);
    }
}
