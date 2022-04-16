package pl.lodz.p.it.dk.exceptions;

public class AccountException extends BaseException {

    private static final String LOGIN_EXISTS_EXCEPTION = "exception.account_exception.login_invalid_exception";
    private static final String EMAIL_EXISTS_EXCEPTION = "exception.account_exception.email_invalid_exception";
    private static final String PHONE_NUMBER_EXISTS_EXCEPTION =
            "exception.account_exception.phone_number_invalid_exception";
    private static final String ALREADY_ACTIVATED_EXCEPTION = "exception.account_exception.already_activated_exception";

    private AccountException(String message) {
        super(message);
    }

    private AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AccountException loginExists(Throwable cause) {
        return new AccountException(LOGIN_EXISTS_EXCEPTION, cause);
    }

    public static AccountException emailExists() {
        return new AccountException(EMAIL_EXISTS_EXCEPTION);
    }

    public static AccountException emailExists(Throwable cause) {
        return new AccountException(EMAIL_EXISTS_EXCEPTION, cause);
    }

    public static AccountException phoneNumberExists(Throwable cause) {
        return new AccountException(PHONE_NUMBER_EXISTS_EXCEPTION, cause);
    }

    public static AccountException alreadyActivated() {
        return new AccountException(ALREADY_ACTIVATED_EXCEPTION);
    }
}
