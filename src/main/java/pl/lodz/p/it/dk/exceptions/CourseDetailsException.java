package pl.lodz.p.it.dk.exceptions;

public class CourseDetailsException extends BaseException {

    private static final String COURSE_CATEGORY_EXISTS_EXCEPTION =
            "exception.course_details_exception.course_category_invalid_exception";

    private CourseDetailsException(String message) {
        super(message);
    }

    private CourseDetailsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CourseDetailsException categoryExists(Throwable cause) {
        return new CourseDetailsException(COURSE_CATEGORY_EXISTS_EXCEPTION, cause);
    }
}
