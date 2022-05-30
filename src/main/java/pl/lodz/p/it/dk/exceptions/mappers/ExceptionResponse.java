package pl.lodz.p.it.dk.exceptions.mappers;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class ExceptionResponse {

    private final String message;
    private Map<String, Set<String>> constraints;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public ExceptionResponse(String message, Map<String, Set<String>> constraints) {
        this(message);
        this.constraints = constraints;
    }
}
