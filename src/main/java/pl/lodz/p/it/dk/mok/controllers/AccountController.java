package pl.lodz.p.it.dk.mok.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.AccountDto;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;
import pl.lodz.p.it.dk.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;
import pl.lodz.p.it.dk.validation.annotations.Code;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountController extends AbstractController {

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @Inject
    private Signer signer;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerAccount(@NotNull @Valid RegisterAccountDto registerAccountDto) throws BaseException {
        repeat(() -> accountEndpoint.registerAccount(registerAccountDto), accountEndpoint);
    }

    @POST
    @Path("/confirm/{code}")
    public void confirmEmail(@NotNull @Code @PathParam("code") @Valid String code) throws BaseException {
        repeat(() -> accountEndpoint.confirmAccount(code), accountEndpoint);
    }

    @PUT
    @RolesAllowed("lockAccount")
    @EtagFilterBinding
    @Path("/lock/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void lockAccount(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        repeat(() -> accountEndpoint.lockAccount(login), accountEndpoint);
    }

    @PUT
    @RolesAllowed("unlockAccount")
    @EtagFilterBinding
    @Path("/unlock/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void unlockAccount(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        repeat(() -> accountEndpoint.unlockAccount(login), accountEndpoint);
    }

    @GET
    @RolesAllowed("getOwnAccountDetails")
    @Path("/getDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnAccountDetails() throws BaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getOwnAccountDetails(), accountEndpoint);
        return Response.ok().entity(accountDto).header("ETag", signer.sign(accountDto)).build();
    }

    @GET
    @RolesAllowed("getOtherAccountDetails")
    @Path("/getDetails/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOtherAccountDetails(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getOtherAccountDetails(login), accountEndpoint);
        return Response.ok().entity(accountDto).header("ETag", signer.sign(accountDto)).build();
    }

}
