package pl.lodz.p.it.dk.exceptions;

public class DrivingLessonException extends BaseException {

    private static final String INVALID_DATE_RANGE_EXCEPTION =
            "exception.driving_lesson_exception.invalid_date_range_exception";
    private static final String CONDITIONS_NOT_MET_EXCEPTION =
            "exception.driving_lesson_exception.conditions_not_met_exception";
    private static final String NO_PERMITS_EXCEPTION = "exception.driving_lesson_exception.no_permits_exception";
    private static final String TIME_FOR_ADDING_EXCEEDED_EXCEPTION =
            "exception.driving_lesson_exception.time_for_adding_exceeded_exception";
    private static final String TOO_MANY_DRIVING_HOURS_EXCEPTION =
            "exception.driving_lesson_exception.too_many_driving_hours_exception";
    private static final String TIME_FOR_CANCELLATION_EXCEEDED_EXCEPTION =
            "exception.driving_lesson_exception.time_for_cancellation_exceeded_exception";
    private static final String INCORRECT_LESSON_STATUS_EXCEPTION =
            "exception.driving_lesson_exception.incorrect_lesson_status_exception";


    private DrivingLessonException(String message) {
        super(message);
    }

    public static DrivingLessonException invalidDateRange() {
        return new DrivingLessonException(INVALID_DATE_RANGE_EXCEPTION);
    }

    public static DrivingLessonException conditionsNotMet() {
        return new DrivingLessonException(CONDITIONS_NOT_MET_EXCEPTION);
    }

    public static DrivingLessonException noPermits() {
        return new DrivingLessonException(NO_PERMITS_EXCEPTION);
    }

    public static DrivingLessonException timeForAddingExceeded() {
        return new DrivingLessonException(TIME_FOR_ADDING_EXCEEDED_EXCEPTION);
    }

    public static DrivingLessonException tooManyDrivingHours() {
        return new DrivingLessonException(TOO_MANY_DRIVING_HOURS_EXCEPTION);
    }

    public static DrivingLessonException timeForCancellationExceeded() {
        return new DrivingLessonException(TIME_FOR_CANCELLATION_EXCEEDED_EXCEPTION);
    }

    public static DrivingLessonException incorrectLessonStatus() {
        return new DrivingLessonException(INCORRECT_LESSON_STATUS_EXCEPTION);
    }

}
