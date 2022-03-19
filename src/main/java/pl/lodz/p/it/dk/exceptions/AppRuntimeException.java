package pl.lodz.p.it.dk.exceptions;

public class AppRuntimeException extends RuntimeException {

    private static final String CONFIG_LOAD_EXCEPTION = "exception.app_runtime_exception.config_load_exception";

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppRuntimeException configLoadException(Throwable cause) {
        return new AppRuntimeException(CONFIG_LOAD_EXCEPTION, cause);
    }
}
