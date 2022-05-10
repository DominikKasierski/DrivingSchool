package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;
import pl.lodz.p.it.dk.mos.endpoints.LectureGroupEndpointLocal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/lectureGroup")
public class LectureGroupController extends AbstractController {

    @Inject
    private LectureGroupEndpointLocal lectureGroupEndpoint;

    @GET
    @RolesAllowed("getTraineeForGroup")
    @Path("/getTraineeForGroup/{courseCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TraineeForGroupDto> getTraineeForGroup(
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
}