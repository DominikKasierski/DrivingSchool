package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.LectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.NewLectureDto;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;
import pl.lodz.p.it.dk.mos.endpoints.LectureGroupEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/lectureGroup")
public class LectureGroupController extends AbstractController {

    @Inject
    private Signer signer;

    @Inject
    private LectureGroupEndpointLocal lectureGroupEndpoint;

    @GET
    @RolesAllowed("getTraineesForGroup")
    @Path("/getTraineesForGroup/{courseCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TraineeForGroupDto> getTraineesForGroup(
            @NotNull @PathParam("courseCategory") CourseCategory courseCategory)
            throws BaseException {
        return repeat(() -> lectureGroupEndpoint.getTraineesForGroup(courseCategory), lectureGroupEndpoint);
    }

    @POST
    @RolesAllowed("createLectureGroup")
    @Path("/createLectureGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createLectureGroup(@NotNull @Valid NewLectureGroupDto newLectureGroupDto) throws BaseException {
        repeat(() -> lectureGroupEndpoint.createLectureGroup(newLectureGroupDto), lectureGroupEndpoint);
    }

    @GET
    @RolesAllowed("getLectureGroups")
    @Path("/getLectureGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LectureGroupDto> getLectureGroups() throws BaseException {
        return repeat(() -> lectureGroupEndpoint.getLectureGroups(), lectureGroupEndpoint);
    }

    @GET
    @RolesAllowed("getLectureGroup")
    @Path("/getLectureGroup/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLectureGroup(@NotNull @PathParam("id") Long id) throws BaseException {
        LectureGroupDto lectureGroupDto = repeat(() -> lectureGroupEndpoint.getLectureGroup(id), lectureGroupEndpoint);
        return Response.ok().entity(lectureGroupDto).header("ETag", signer.sign(lectureGroupDto)).build();
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("assignToLectureGroup")
    @Path("/assignToLectureGroup/{lectureGroupId}/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void assignToLectureGroup(@NotNull @PathParam("lectureGroupId") Long lectureGroupId,
                                     @NotNull @PathParam("courseId") Long courseId) throws BaseException {
        repeat(() -> lectureGroupEndpoint.assignToLectureGroup(lectureGroupId, courseId), lectureGroupEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("addLectureForGroup")
    @Path("/addLectureForGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public void addLectureForGroup(@NotNull @Valid NewLectureDto newLectureDto) throws BaseException {
        repeat(() -> lectureGroupEndpoint.addLectureForGroup(newLectureDto), lectureGroupEndpoint);
    }
}