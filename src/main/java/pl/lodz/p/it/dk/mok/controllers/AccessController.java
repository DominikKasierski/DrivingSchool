package pl.lodz.p.it.dk.mok.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.AccessesDto;
import pl.lodz.p.it.dk.mok.dtos.InstructorAccessDto;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;
import pl.lodz.p.it.dk.mok.endpoints.AccessEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/access")
public class AccessController extends AbstractController {

    @Inject
    private Signer signer;

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

    @PUT
    @RolesAllowed("revokeAccessType")
    @EtagFilterBinding
    @Path("/revoke/{login}/{accessType}")
    public void revokeAccessType(@NotNull @Login @PathParam("login") @Valid String login,
                                 @NotNull @PathParam("accessType") AccessType accessType) throws BaseException {
        repeat(() -> accessEndpoint.revokeAccessType(login, accessType), accessEndpoint);
    }

    @GET
    @RolesAllowed("getOwnAccesses")
    @Path("/getAccesses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnAccesses() throws BaseException {
        AccessesDto accessesDto = repeat(() -> accessEndpoint.getOwnAccesses(), accessEndpoint);
        return Response.ok().entity(accessesDto).header("ETag", signer.sign(accessesDto)).build();
    }

    @GET
    @RolesAllowed("getOtherAccesses")
    @Path("/getAccesses/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOtherAccesses(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        AccessesDto accessesDto = repeat(() -> accessEndpoint.getOtherAccesses(login), accessEndpoint);
        return Response.ok().entity(accessesDto).header("ETag", signer.sign(accessesDto)).build();
    }

    @GET
    @RolesAllowed("switchAccessType")
    @Path("switchAccessType/{accessType}")
    public Response switchAccessType(@NotNull @PathParam("accessType") AccessType accessType) {
        accessEndpoint.switchAccessType(accessType);
        return Response.ok().build();
    }

    @GET
    @RolesAllowed("getTraineeAccess")
    @Path("getTraineeAccess")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTraineeAccess() throws BaseException {
        TraineeAccessDto traineeAccessDto = repeat(() -> accessEndpoint.getTraineeAccess(), accessEndpoint);
        return Response.ok().entity(traineeAccessDto).header("ETag", signer.sign(traineeAccessDto)).build();
    }

    @GET
    @RolesAllowed("getAllInstructors")
    @Path("/getInstructors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<InstructorAccessDto> getAllInstructors() throws BaseException {
        return repeat(() -> accessEndpoint.getAllInstructors(), accessEndpoint);
    }

    @GET
    @RolesAllowed("getInstructorAccess")
    @Path("/getInstructorAccess/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInstructorAccess(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        InstructorAccessDto instructorAccessDto =
                repeat(() -> accessEndpoint.getInstructorAccess(login), accessEndpoint);
        return Response.ok().entity(instructorAccessDto).header("ETag", signer.sign(instructorAccessDto)).build();
    }

    @GET
    @RolesAllowed("getOwnPermissions")
    @Path("/getOwnPermissions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnPermissions() throws BaseException {
        String permissions = repeat(() -> accessEndpoint.getOwnPermissions(), accessEndpoint);
        return Response.ok().entity(permissions).build();
    }

    @PUT
    @RolesAllowed("addInstructorPermission")
    @EtagFilterBinding
    @Path("/addInstructorPermission/{login}/{courseCategory}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addInstructorPermission(@NotNull @Login @PathParam("login") @Valid String login,
                                        @NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        repeat(() -> accessEndpoint.addInstructorPermission(login, courseCategory), accessEndpoint);
    }

    @PUT
    @RolesAllowed("removeInstructorPermission")
    @EtagFilterBinding
    @Path("/removeInstructorPermission/{login}/{courseCategory}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeInstructorPermission(@NotNull @Login @PathParam("login") @Valid String login,
                                           @NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        repeat(() -> accessEndpoint.removeInstructorPermission(login, courseCategory), accessEndpoint);
    }
}
