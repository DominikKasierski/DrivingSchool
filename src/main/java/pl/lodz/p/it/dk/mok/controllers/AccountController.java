package pl.lodz.p.it.dk.mok.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.*;
import pl.lodz.p.it.dk.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;
import pl.lodz.p.it.dk.validation.annotations.Code;
import pl.lodz.p.it.dk.validation.annotations.EmailAddress;
import pl.lodz.p.it.dk.validation.annotations.Language;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/account")
public class  AccountController extends AbstractController {

    @Inject
    private Signer signer;

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerAccount(@NotNull @Valid RegisterAccountDto registerAccountDto) throws BaseException {
        repeat(() -> accountEndpoint.registerAccount(registerAccountDto), accountEndpoint);
    }

    @PUT
    @Path("/confirmAccount/{code}")
    public void confirmAccount(@NotNull @Code @PathParam("code") @Valid String code) throws BaseException {
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
    public Response getOtherAccountDetails(@NotNull @Login @PathParam("login") @Valid String login)
            throws BaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getOtherAccountDetails(login), accountEndpoint);
        return Response.ok().entity(accountDto).header("ETag", signer.sign(accountDto)).build();
    }

    @GET
    @RolesAllowed("getAllAccounts")
    @Path("/getAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> getAllAccounts() throws BaseException {
        return repeat(() -> accountEndpoint.getAllAccounts(), accountEndpoint);
    }

    @PUT
    @RolesAllowed("editPersonalData")
    @EtagFilterBinding
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editPersonalData(@NotNull @Valid PersonalDataDto personalDataDto) throws BaseException {
        repeat(() -> accountEndpoint.editPersonalData(personalDataDto), accountEndpoint);
    }

    @PUT
    @RolesAllowed("editOwnEmail")
    @EtagFilterBinding
    @Path("/editEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editOwnEmail(@NotNull @Valid NewEmailDto newEmailDto) throws BaseException {
        repeat(() -> accountEndpoint.editOwnEmail(newEmailDto), accountEndpoint);
    }

    @PUT
    @RolesAllowed("editOtherEmail")
    @EtagFilterBinding
    @Path("/editEmail/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editOtherEmail(@NotNull @Login @PathParam("login") @Valid String login,
                               @NotNull @Valid NewEmailDto newEmailDto) throws BaseException {
        repeat(() -> accountEndpoint.editOtherEmail(login, newEmailDto), accountEndpoint);
    }

    @PUT
    @Path("/confirmEmail/{code}")
    public void confirmEmail(@NotNull @Code @PathParam("code") @Valid String code) throws BaseException {
        repeat(() -> accountEndpoint.confirmEmail(code), accountEndpoint);
    }

    @PUT
    @RolesAllowed("changePassword")
    @EtagFilterBinding
    @Path("/changePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changePassword(@NotNull @Valid ChangePasswordDto changePasswordDto) throws BaseException {
        repeat(() -> accountEndpoint.changePassword(changePasswordDto), accountEndpoint);
    }

    @PUT
    @Path("/resetPassword/{email}")
    public void resetPassword(@NotNull @EmailAddress @PathParam("email") @Valid String email) throws BaseException {
        repeat(() -> accountEndpoint.resetPassword(email), accountEndpoint);
    }

    @PUT
    @Path("/confirmPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public void confirmPasswordChange(@NotNull @Valid ConfirmPasswordChangeDto confirmPasswordChangeDto)
            throws BaseException {
        repeat(() -> accountEndpoint.confirmPasswordChange(confirmPasswordChangeDto), accountEndpoint);
    }

    @PUT
    @RolesAllowed("editLanguage")
    @EtagFilterBinding
    @Path("/editLanguage/{lang}")
    public void editLanguage(@NotNull @Language @PathParam("lang") String language) throws BaseException {
        repeat(() -> accountEndpoint.editLanguage(language), accountEndpoint);
    }
}
