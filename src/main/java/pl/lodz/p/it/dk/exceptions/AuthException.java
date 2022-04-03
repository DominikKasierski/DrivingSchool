package pl.lodz.p.it.dk.exceptions;

public class AuthException extends BaseException {

    private static final String INVALID_CREDENTIALS = "exception.auth_exception.invalid_credentials";

    private AuthException(String message) {
        super(message);
    }

    public static AuthException invalidCredentials() {
        return new AuthException(INVALID_CREDENTIALS);
    }
}
