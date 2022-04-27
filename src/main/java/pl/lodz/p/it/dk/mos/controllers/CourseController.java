package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.endpoints.CourseEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;


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
}
