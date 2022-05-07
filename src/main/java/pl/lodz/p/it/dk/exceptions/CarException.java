package pl.lodz.p.it.dk.exceptions;

public class CarException extends BaseException {

    private static final String CAR_IN_USE_EXCEPTION = "exception.car_in_use_exception.email_exception";

    private CarException(String message) {
        super(message);
    }

    public static CarException carInUse() {
        return new CarException(CAR_IN_USE_EXCEPTION);
    }
}
