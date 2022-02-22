package pl.lodz.p.it.dk.exceptions;

public class CourseException extends BaseException {

    private static final String ALREADY_ASSIGNED_EXCEPTION =
            "exception.course_exception.already_assigned_exception";

    private CourseException(String message) {
        super(message);
    }

    private CourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CourseException alreadyAssigned(Throwable cause) {
        return new CourseException(ALREADY_ASSIGNED_EXCEPTION, cause);
    }

}
