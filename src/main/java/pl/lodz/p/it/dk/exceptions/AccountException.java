package pl.lodz.p.it.dk.exceptions;

public class AccountException extends BaseException {

    private static final String LOGIN_EXISTS_EXCEPTION = "exception.account_exception.login_invalid_exception";
    private static final String EMAIL_EXISTS_EXCEPTION = "exception.account_exception.email_invalid_exception";
    private static final String PHONE_NUMBER_EXISTS_EXCEPTION =
            "exception.account_exception.phone_number_invalid_exception";
    private static final String ALREADY_ACTIVATED_EXCEPTION = "exception.account_exception.already_activated_exception";
    private static final String WRONG_PASSWORD_EXCEPTION = "exception.account_exception.wrong_password_exception";
    private static final String SAME_PASSWORD_EXCEPTION = "exception.account_exception.same_password_exception";
    private static final String UNCONFIRMED_ACCOUNT_EXCEPTION = "exception.account_exception.unconfirmed_account_exception";
    private static final String BLOCKED_ACCOUNT_EXCEPTION = "exception.account_exception.blocked_account_exception";


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

    public static AccountException wrongPassword() {
        return new AccountException(WRONG_PASSWORD_EXCEPTION);
    }

    public static AccountException samePassword() {
        return new AccountException(SAME_PASSWORD_EXCEPTION);
    }

    public static AccountException accountUnconfirmed() {
        return new AccountException(UNCONFIRMED_ACCOUNT_EXCEPTION);
    }

    public static AccountException accountBlocked() {
        return new AccountException(BLOCKED_ACCOUNT_EXCEPTION);
    }
}
