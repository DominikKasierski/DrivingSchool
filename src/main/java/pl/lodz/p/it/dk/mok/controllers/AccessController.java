package pl.lodz.p.it.dk.mok.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.endpoints.AccessEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/access")
public class AccessController extends AbstractController {

    @Inject
    private AccessEndpointLocal accessEndpoint;

    @PUT
    @RolesAllowed("grantAccessType")
    @EtagFilterBinding
    @Path("/grant/{login}/{accessType}")
    public void grantAccessType(@NotNull @Login @PathParam("login") @Valid String login,
                                @NotNull @PathParam("accessType") AccessType accessType) throws BaseException {
        repeat(() -> accessEndpoint.grantAccessType(login, accessType), accessEndpoint);
    }

    @PATCH
    @RolesAllowed("revokeAccessType")
    @EtagFilterBinding
    @Path("/revoke/{login}/{accessType}")
    public void revokeAccessType(@NotNull @Login @PathParam("login") @Valid String login,
                                 @NotNull @PathParam("accessType") AccessType accessType) throws BaseException {
        repeat(() -> accessEndpoint.revokeAccessType(login, accessType), accessEndpoint);
    }

    @GET
    @RolesAllowed("switchAccessType")
    @Path("switchAccessType/{accessType}")
    public Response switchAccessType(@NotNull @PathParam("accessType") AccessType accessType) {
        accessEndpoint.switchAccessType(accessType);
        return Response.ok().build();
    }
}
