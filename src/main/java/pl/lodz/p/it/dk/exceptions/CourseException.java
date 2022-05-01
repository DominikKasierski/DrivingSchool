package pl.lodz.p.it.dk.exceptions;

public class CourseException extends BaseException {

    private static final String ALREADY_ASSIGNED_EXCEPTION = "exception.course_exception.already_assigned_exception";
    private static final String ALREADY_COMPLETED_EXCEPTION = "exception.course_exception.already_completed_exception";
    private static final String NO_ONGOING_COURSE_EXCEPTION = "exception.course_exception.no_ongoing_course_exception";

    private CourseException(String message) {
        super(message);
    }

    private CourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CourseException alreadyAssigned() {
        return new CourseException(ALREADY_ASSIGNED_EXCEPTION);
    }

    public static CourseException alreadyAssigned(Throwable cause) {
        return new CourseException(ALREADY_ASSIGNED_EXCEPTION, cause);
    }

    public static CourseException alreadyCompleted() {
        return new CourseException(ALREADY_COMPLETED_EXCEPTION);
    }

    public static CourseException noOngoingCourse() {
        return new CourseException(NO_ONGOING_COURSE_EXCEPTION);
    }
}
