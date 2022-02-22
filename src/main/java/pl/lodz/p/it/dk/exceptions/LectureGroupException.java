package pl.lodz.p.it.dk.exceptions;

public class LectureGroupException extends BaseException {

    private static final String NAME_EXISTS_EXCEPTION = "exception.lecture_group_exception.name_invalid_exception";

    private LectureGroupException(String message) {
        super(message);
    }

    private LectureGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public static LectureGroupException nameExists(Throwable cause) {
        return new LectureGroupException(NAME_EXISTS_EXCEPTION, cause);
    }
}
