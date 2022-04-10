package pl.lodz.p.it.dk.exceptions;

public class ConfirmationCodeException extends BaseException {

    private static final String CODE_EXISTS_EXCEPTION = "exception.confirmation_code_exception.code_invalid_exception";
    private static final String CODE_USED_EXCEPTION = "exception.confirmation_code_exception.code_used_exception";
    private static final String WRONG_CODE_TYPE_EXCEPTION =
            "exception.confirmation_code_exception.wrong_code_type_exception";

    private ConfirmationCodeException(String message) {
        super(message);
    }

    private ConfirmationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ConfirmationCodeException codeExists(Throwable cause) {
        return new ConfirmationCodeException(CODE_EXISTS_EXCEPTION, cause);
    }

    public static ConfirmationCodeException codeUsed() {
        return new ConfirmationCodeException(CODE_USED_EXCEPTION);
    }

    public static ConfirmationCodeException wrongCodeType() {
        return new ConfirmationCodeException(WRONG_CODE_TYPE_EXCEPTION);
    }

}
