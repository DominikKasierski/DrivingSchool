package pl.lodz.p.it.dk.exceptions;

public class LectureGroupException extends BaseException {

    private static final String NAME_EXISTS_EXCEPTION = "exception.lecture_group_exception.name_invalid_exception";
    private static final String ALREADY_ASSIGNED_EXCEPTION =
            "exception.lecture_group_exception.already_assigned_exception";
    private static final String UNPAID_COURSE_EXCEPTION =
            "exception.lecture_group_exception.unpaid_course_exception";
    private static final String LECTURES_STARTED_EXCEPTION =
            "exception.lecture_group_exception.lectures_already_started_exception";
    private static final String INVALID_DATE_RANGE_EXCEPTION =
            "exception.lecture_group_exception.invalid_date_range_exception";
    private static final String TIME_FOR_ADDING_EXCEEDED_EXCEPTION =
            "exception.lecture_group_exception.time_for_adding_exceeded_exception";
    private static final String NO_PERMITS_EXCEPTION = "exception.lecture_group_exception.no_permits_exception";
    private static final String DATE_RANGES_OVERLAP_EXCEPTION =
            "exception.lecture_group_exception.date_ranges_overlap_exception";
    private static final String TOO_MANY_LECTURE_HOURS_EXCEPTION =
            "exception.lecture_group_exception.too_many_lecture_hours_exception";

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

    public static LectureGroupException invalidDateRange() {
        return new LectureGroupException(INVALID_DATE_RANGE_EXCEPTION);
    }

    public static LectureGroupException timeForAddingExceeded() {
        return new LectureGroupException(TIME_FOR_ADDING_EXCEEDED_EXCEPTION);
    }

    public static LectureGroupException noPermits() {
        return new LectureGroupException(NO_PERMITS_EXCEPTION);
    }

    public static LectureGroupException dateRangesOverlap() {
        return new LectureGroupException(DATE_RANGES_OVERLAP_EXCEPTION);
    }

    public static LectureGroupException tooManyLectureHours() {
        return new LectureGroupException(TOO_MANY_LECTURE_HOURS_EXCEPTION);
    }
}
