package pl.lodz.p.it.dk.exceptions;

public class ConfirmationCodeException extends BaseException {

    private static final String CODE_EXISTS_EXCEPTION = "exception.confirmation_code_exception.code_invalid_exception";

    private ConfirmationCodeException(String message) {
        super(message);
    }

    private ConfirmationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ConfirmationCodeException codeExists(Throwable cause) {
        return new ConfirmationCodeException(CODE_EXISTS_EXCEPTION, cause);
    }

}
