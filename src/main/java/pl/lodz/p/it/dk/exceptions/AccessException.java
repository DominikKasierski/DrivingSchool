package pl.lodz.p.it.dk.exceptions;

public class AccessException extends BaseException {

    private static final String ALREADY_GRANTED_EXCEPTION = "exception.access_exception.already_granted_exception";

    private AccessException(String message) {
        super(message);
    }

    private AccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AccessException alreadyGranted(Throwable cause) {
        return new AccessException(ALREADY_GRANTED_EXCEPTION, cause);
    }

}
