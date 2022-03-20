package pl.lodz.p.it.dk.exceptions;

public class AppRuntimeException extends RuntimeException {

    private static final String CONFIG_LOAD_EXCEPTION = "exception.app_runtime_exception.config_load_exception";
    private static final String JWT_EXCEPTION = "exception.app_runtime_exception.jwt_exception";
    private static final String ETAG_EXCEPTION = "exception.app_runtime_exception.etag_exception";

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppRuntimeException configLoadException(Throwable cause) {
        return new AppRuntimeException(CONFIG_LOAD_EXCEPTION, cause);
    }

    public static AppRuntimeException jwtException(Throwable cause) {
        return new AppRuntimeException(JWT_EXCEPTION, cause);
    }

    public static AppRuntimeException etagException(Throwable cause) {
        return new AppRuntimeException(ETAG_EXCEPTION, cause);
    }
}
