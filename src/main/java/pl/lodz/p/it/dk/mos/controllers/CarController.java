package pl.lodz.p.it.dk.mos.controllers;

import pl.lodz.p.it.dk.common.abstracts.AbstractController;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.dtos.CarDto;
import pl.lodz.p.it.dk.mos.dtos.EditCarDto;
import pl.lodz.p.it.dk.mos.dtos.NewCarDto;
import pl.lodz.p.it.dk.mos.endpoints.CarEndpointLocal;
import pl.lodz.p.it.dk.security.etag.EtagFilterBinding;
import pl.lodz.p.it.dk.security.etag.Signer;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/car")
public class CarController extends AbstractController {

    @Inject
    private Signer signer;

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
    @RolesAllowed("removeCar")
    @Path("/removeCar/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeCar(@NotNull @PathParam("id") Long id) throws BaseException {
        repeat(() -> carEndpoint.removeCar(id), carEndpoint);
    }

    @PUT
    @EtagFilterBinding
    @RolesAllowed("editCar")
    @Path("/editCar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editCar(@NotNull @Valid EditCarDto editCarDto) throws BaseException {
        repeat(() -> carEndpoint.editCar(editCarDto), carEndpoint);
    }

    @GET
    @RolesAllowed("getCar")
    @Path("/getCar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCar(@NotNull @PathParam("id") Long id) throws BaseException {
        CarDto carDto = carEndpoint.getCar(id);
        return Response.ok().entity(carDto).header("ETag", signer.sign(carDto)).build();
    }

}
