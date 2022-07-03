package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CourseDetailsDto;
import pl.lodz.p.it.dk.mos.endpoints.CourseDetailsEndpointLocal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/courseDetails")
public class CourseDetailsController extends AbstractController {

    @Inject
    private CourseDetailsEndpointLocal courseDetailsEndpoint;

    @GET
    @RolesAllowed("getCoursesDetails")
    @Path("/getCoursesDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CourseDetailsDto> getAllCoursesDetails() throws BaseException {
        return repeat(() -> courseDetailsEndpoint.getAllCoursesDetails(), courseDetailsEndpoint);
    }
}
