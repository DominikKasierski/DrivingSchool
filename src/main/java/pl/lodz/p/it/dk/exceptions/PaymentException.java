package pl.lodz.p.it.dk.exceptions;

public class PaymentException extends BaseException {

    private static final String PAYMENT_IN_PROGRESS_EXCEPTION =
            "exception.payment_exception.payment_in_progress_exception";
    private static final String COURSE_OVERPAID_EXCEPTION = "exception.payment_exception.course_overpaid_exception";
    private static final String NO_PAYMENT_IN_PROGRESS_EXCEPTION =
            "exception.payment_exception.no_payment_in_progress_exception";

    private PaymentException(String message) {
        super(message);
    }

    public static PaymentException paymentInProgress() {
        return new PaymentException(PAYMENT_IN_PROGRESS_EXCEPTION);
    }

    public static PaymentException courseOverpaid() {
        return new PaymentException(COURSE_OVERPAID_EXCEPTION);
    }

    public static PaymentException noPaymentInProgress() {
        return new PaymentException(NO_PAYMENT_IN_PROGRESS_EXCEPTION);
    }
}
