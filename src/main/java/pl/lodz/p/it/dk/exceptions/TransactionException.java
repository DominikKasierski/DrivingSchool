package pl.lodz.p.it.dk.exceptions;

public class TransactionException extends BaseException {

    private static final String EXCEEDING_REPETITION_LIMIT_EXCEPTION =
            "exception.transaction_exception.exceeding_repetition_limit_exception";

    private TransactionException(String message) {
        super(message);
    }

    public static TransactionException limitExceeded() {
        return new TransactionException(EXCEEDING_REPETITION_LIMIT_EXCEPTION);
    }
}
