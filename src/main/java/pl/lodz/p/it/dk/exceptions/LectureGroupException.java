package pl.lodz.p.it.dk.exceptions;

public class LectureGroupException extends BaseException {

    private static final String NAME_EXISTS_EXCEPTION = "exception.lecture_group_exception.name_invalid_exception";
    private static final String ALREADY_ASSIGNED_EXCEPTION =
            "exception.lecture_group_exception.already_assigned_exception";
    private static final String UNPAID_COURSE_EXCEPTION =
            "exception.lecture_group_exception.already_assigned_exception";
    private static final String LECTURES_STARTED_EXCEPTION =
            "exception.lecture_group_exception.already_assigned_exception";

    private LectureGroupException(String message) {
        super(message);
    }

    private LectureGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public static LectureGroupException nameExists(Throwable cause) {
        return new LectureGroupException(NAME_EXISTS_EXCEPTION, cause);
    }

    public static LectureGroupException alreadyAssigned() {
        return new LectureGroupException(ALREADY_ASSIGNED_EXCEPTION);
    }

    public static LectureGroupException unpaidCourse() {
        return new LectureGroupException(UNPAID_COURSE_EXCEPTION);
    }

    public static LectureGroupException lecturesStarted() {
        return new LectureGroupException(LECTURES_STARTED_EXCEPTION);
    }
}
