package pl.lodz.p.it.dk.mok.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;
import pl.lodz.p.it.dk.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.validation.annotations.Code;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountController extends AbstractController {

    @Inject
    private AccountEndpointLocal accountEndpoint;

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

}
