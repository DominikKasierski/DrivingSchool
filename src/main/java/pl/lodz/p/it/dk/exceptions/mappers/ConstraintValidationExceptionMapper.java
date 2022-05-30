package pl.lodz.p.it.dk.exceptions.mappers;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private final static String REST_VALIDATION_ERROR = "error.rest.validation";

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Set<String>> constraints = new HashMap<>();

        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            String propertyName = getPropertyName(violation.getPropertyPath());
            Set<String> currentViolation = constraints.getOrDefault(propertyName, new HashSet<>());
            currentViolation.add(violation.getMessage());
            constraints.put(propertyName, currentViolation);
        }

        return Response.status(BAD_REQUEST).entity(new ExceptionResponse(REST_VALIDATION_ERROR, constraints)).build();
    }

    private String getPropertyName(Path path) {
        String name = null;

        for (Path.Node node : path) {
            name = node.getName();
        }

        return name;
    }
}
