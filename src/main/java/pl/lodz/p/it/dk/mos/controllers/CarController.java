package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.EditCarDto;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;
import pl.lodz.p.it.dk.mos.endpoints.CarEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/car")
public class CarController extends AbstractController {

    @Inject
    private CarEndpointLocal carEndpoint;

    @POST
    @EtagFilterBinding
    @RolesAllowed("addCar")
    @Path("/addCar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addCar(@NotNull @Valid NewCarDto newCarDto) throws BaseException {
        repeat(() -> carEndpoint.addCar(newCarDto), carEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("editCar")
    @Path("/editCar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editCar(@NotNull @Valid EditCarDto editCarDto) throws BaseException {
        repeat(() -> carEndpoint.editCar(editCarDto), carEndpoint);
    }

}
