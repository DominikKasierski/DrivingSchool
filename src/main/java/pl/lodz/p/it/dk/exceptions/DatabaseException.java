package pl.lodz.p.it.dk.exceptions;

public class DatabaseException extends BaseException {

    private static final String QUERY_EXCEPTION = "exception.database_exception.query_exception";

    private DatabaseException(String message) {
        super(message);
    }

    private DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DatabaseException queryException() {
        return new DatabaseException(QUERY_EXCEPTION);
    }

    public static DatabaseException queryException(Throwable cause) {
        return new DatabaseException(QUERY_EXCEPTION, cause);
    }
}
