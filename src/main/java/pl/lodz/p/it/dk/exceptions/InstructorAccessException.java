package pl.lodz.p.it.dk.exceptions;

public class InstructorAccessException extends BaseException {

    private static final String PERMISSION_ALREADY_ADDED_EXCEPTION =
            "exception.instructor_access_exception.permission_already_added_exception";
    private static final String PERMISSION_ALREADY_REMOVED_EXCEPTION =
            "exception.instructor_access_exception.permission_granted_exception";

    private InstructorAccessException(String message) {
        super(message);
    }

    public static InstructorAccessException permissionAlreadyAdded() {
        return new InstructorAccessException(PERMISSION_ALREADY_ADDED_EXCEPTION);
    }

    public static InstructorAccessException permissionAlreadyRemoved() {
        return new InstructorAccessException(PERMISSION_ALREADY_REMOVED_EXCEPTION);
    }
}
