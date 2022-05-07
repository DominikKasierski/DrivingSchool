package pl.lodz.p.it.dk.exceptions;

public class EmailException extends BaseException {

    private static final String EMAIL_EXCEPTION = "exception.email_exception.email_exception";

    private EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmailException emailException(Throwable cause) {
        return new EmailException(EMAIL_EXCEPTION, cause);
    }
}
