package pl.lodz.p.it.dk.exceptions;

public class AppOptimisticLockException extends BaseException {

    private static final String OPTIMISTIC_LOCK_EXCEPTION =
            "exception.app_optimistic_lock_exception.optimistic_lock_exception";

    private AppOptimisticLockException(String message) {
        super(message);
    }

    private AppOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppOptimisticLockException optimisticLockException() {
        return new AppOptimisticLockException(OPTIMISTIC_LOCK_EXCEPTION);
    }

    public static AppOptimisticLockException optimisticLockException(Throwable cause) {
        return new AppOptimisticLockException(OPTIMISTIC_LOCK_EXCEPTION, cause);
    }
}
