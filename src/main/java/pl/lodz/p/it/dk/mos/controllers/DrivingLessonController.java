package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.NewDrivingLesson;
import pl.lodz.p.it.dk.mos.endpoints.DrivingLessonEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/drivingLesson")
public class DrivingLessonController extends AbstractController {

    @Inject
    private DrivingLessonEndpointLocal drivingLessonEndpoint;

    @POST
    @EtagFilterBinding
    @RolesAllowed("addDrivingLesson")
    @Path("/addDrivingLesson")
    @Produces(MediaType.APPLICATION_JSON)
    public void addDrivingLesson(@NotNull @Valid NewDrivingLesson newDrivingLesson) throws BaseException {
        repeat(() -> drivingLessonEndpoint.addDrivingLesson(newDrivingLesson), drivingLessonEndpoint);
    }
}
