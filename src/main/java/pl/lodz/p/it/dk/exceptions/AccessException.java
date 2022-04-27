package pl.lodz.p.it.dk.exceptions;

public class AccessException extends BaseException {

    private static final String ALREADY_GRANTED_EXCEPTION = "exception.access_exception.already_granted_exception";
    private static final String ALREADY_REVOKED_EXCEPTION = "exception.access_exception.already_revoked_exception";
    private static final String UNSUPPORTED_COMBINATION_EXCEPTION =
            "exception.access_exception.unsupported_combination_exception";
    private static final String UNSUPPORTED_ACCESS_TYPE_EXCEPTION =
            "exception.access_exception.unsupported_access_type_exception";
    private static final String NO_PROPER_ACCESS = "exception.access_exception.no_proper_access_exception";

    private AccessException(String message) {
        super(message);
    }

    private AccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AccessException alreadyGranted() {
        return new AccessException(ALREADY_GRANTED_EXCEPTION);
    }

    public static AccessException alreadyGranted(Throwable cause) {
        return new AccessException(ALREADY_GRANTED_EXCEPTION, cause);
    }

    public static AccessException alreadyRevoked() {
        return new AccessException(ALREADY_REVOKED_EXCEPTION);
    }

    public static AccessException unsupportedCombination() {
        return new AccessException(UNSUPPORTED_COMBINATION_EXCEPTION);
    }

    public static AccessException unsupportedAccessType() {
        return new AccessException(UNSUPPORTED_ACCESS_TYPE_EXCEPTION);
    }

    public static AccessException noProperAccess() {
        return new AccessException(NO_PROPER_ACCESS);
    }
}
