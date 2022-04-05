package pl.lodz.p.it.dk.auth.controllers;

import pl.lodz.p.it.dk.auth.dtos.LoginDataDto;
import pl.lodz.p.it.dk.auth.endpoints.AuthEndpointLocal;
import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.AuthException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;
import pl.lodz.p.it.dk.mok.endpoints.AccountEndpointLocal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthController extends AbstractController {

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    private AuthEndpointLocal authEndpoint;

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public Response login(@NotNull @Valid LoginDataDto loginDataDto) throws BaseException {
        try {
            String token = authEndpoint.login(loginDataDto);
            String language = servletRequest.getLocale().toString().substring(0, 2);
            repeat(() -> accountEndpoint.updateAuthInfo(loginDataDto.getLogin(), language), accountEndpoint);
            return Response.ok().type("application/json").entity(token).build();
        } catch (AuthException e) {
            try {
                repeat(() -> accountEndpoint.updateAuthInfo(loginDataDto.getLogin()), accountEndpoint);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } catch (NotFoundException notFoundException) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
    }

    @GET
    @RolesAllowed("logout")
    @Path("logout")
    public Response logout() {
        authEndpoint.logout();
        return Response.ok().build();
    }

    @POST
    @Path("updateToken")
    public Response updateToken(@NotNull String currentToken) {
        String updatedToken = authEndpoint.updateToken(currentToken);
        return Response.ok().entity(updatedToken).build();
    }
}
