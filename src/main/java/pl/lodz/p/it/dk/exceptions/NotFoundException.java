package pl.lodz.p.it.dk.exceptions;

public class NotFoundException extends BaseException {

    private static final String ACCOUNT_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.account_not_found_exception";
    private static final String CONFIRMATION_CODE_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.confirmation_code_not_found_exception";
    private static final String CAR_NOT_FOUND_EXCEPTION = "exception.not_found_exception.car_not_found_exception";
    private static final String COURSE_DETAILS_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.course_details_not_found_exception";
    private static final String COURSE_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.course_details_not_found_exception";
    private static final String LECTURE_GROUP_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.lecture_group_not_found_exception";
    private static final String PAYMENT_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.payment_not_found_exception";
    private static final String DRIVING_LESSON_NOT_FOUND_EXCEPTION =
            "exception.not_found_exception.driving_lesson_not_found_exception";

    private NotFoundException(String message) {
        super(message);
    }

    private NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException accountNotFound(Throwable cause) {
        return new NotFoundException(ACCOUNT_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException confirmationCodeNotFound(Throwable cause) {
        return new NotFoundException(CONFIRMATION_CODE_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException carNotFound(Throwable cause) {
        return new NotFoundException(CAR_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException courseDetailsNotFound(Throwable cause) {
        return new NotFoundException(COURSE_DETAILS_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException courseNotFound(Throwable cause) {
        return new NotFoundException(COURSE_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException lectureGroupNotFound(Throwable cause) {
        return new NotFoundException(LECTURE_GROUP_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException paymentNotFound(Throwable cause) {
        return new NotFoundException(PAYMENT_NOT_FOUND_EXCEPTION, cause);
    }

    public static NotFoundException drivingLessonNotFound(Throwable cause) {
        return new NotFoundException(DRIVING_LESSON_NOT_FOUND_EXCEPTION, cause);
    }

}
