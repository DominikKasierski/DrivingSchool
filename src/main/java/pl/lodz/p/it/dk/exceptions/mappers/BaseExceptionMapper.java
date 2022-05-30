package pl.lodz.p.it.dk.exceptions.mappers;

import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BaseExceptionMapper implements ExceptionMapper<BaseException> {

    @Override
    public Response toResponse(BaseException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionResponse(exception.getMessage())).build();
    }
}
