package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;
import pl.lodz.p.it.dk.mos.endpoints.CourseEndpointLocal;
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

@Path("/course")
public class CourseController extends AbstractController {

    @Inject
    private Signer signer;

    @Inject
    private CourseEndpointLocal courseEndpoint;

    @POST
    @EtagFilterBinding
    @RolesAllowed("createCourse")
    @Path("/createCourse/{courseCategory}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createCourse(@NotNull @PathParam("courseCategory") CourseCategory courseCategory) throws BaseException {
        repeat(() -> courseEndpoint.createCourse(courseCategory), courseEndpoint);
    }

    @GET
    @RolesAllowed("getOwnCourse")
    @Path("/getCourse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnCourse() throws BaseException {
        CourseDto courseDto = repeat(() -> courseEndpoint.getOwnCourse(), courseEndpoint);
        return Response.ok().entity(courseDto).header("ETag", signer.sign(courseDto)).build();
    }

    @GET
    @RolesAllowed("getOtherCourse")
    @Path("/getCourse/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOtherCourse(@NotNull @Login @PathParam("login") @Valid String login) throws BaseException {
        CourseDto courseDto = repeat(() -> courseEndpoint.getOtherCourse(login), courseEndpoint);
        return Response.ok().entity(courseDto).header("ETag", signer.sign(courseDto)).build();
    }

    @GET
    @RolesAllowed("getTraineeForGroup")
    @Path("/getTraineeForGroup/{courseCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TraineeForGroupDto> getTraineeForGroup(@NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        return repeat(() -> courseEndpoint.getTraineesForGroup(courseCategory), courseEndpoint);
    }
}
