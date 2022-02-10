package pl.lodz.p.it.dk.exceptions;

public class NotFoundException extends BaseException {

    private static final String CONFIRMATION_CODE_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.confirmation_code_not_found_exception";

    private NotFoundException(String message) {
        super(message);
    }

    private NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException confirmationCodeNotFound(Throwable cause) {
        return new NotFoundException(CONFIRMATION_CODE_NOT_FOUND_EXCEPTION, cause);
    }

}
