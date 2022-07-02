package pl.lodz.p.it.dk.exceptions;

public class CarException extends BaseException {

    private static final String REGISTRATION_NUMBER_EXISTS_EXCEPTION =
            "exception.car_exception.registration_number_exists_exception";
    private static final String CAR_IN_USE_EXCEPTION = "exception.car_exception.car_in_use_exception";
    private static final String NO_CAR_AVAILABLE_EXCEPTION = "exception.car_exception.car_in_use_exception";

    private CarException(String message) {
        super(message);
    }

    private CarException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CarException registrationNumberExists(Throwable cause) {
        return new CarException(REGISTRATION_NUMBER_EXISTS_EXCEPTION, cause);
    }

    public static CarException carInUse() {
        return new CarException(CAR_IN_USE_EXCEPTION);
    }

    public static CarException noCarAvailable() {
        return new CarException(NO_CAR_AVAILABLE_EXCEPTION);
    }


}
