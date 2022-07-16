package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.*;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;
import pl.lodz.p.it.dk.entities.enums.PaymentStatus;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.CourseException;
import pl.lodz.p.it.dk.mos.dtos.BriefCourseInfoDto;
import pl.lodz.p.it.dk.mos.dtos.CourseStatisticsDto;
import pl.lodz.p.it.dk.mos.dtos.InstructorStatisticsDto;
import pl.lodz.p.it.dk.mos.facades.CourseFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourseManager {

    @Inject
    CourseDetailsManager courseDetailsManager;

    @Inject
    TraineeAccessManager traineeAccessManager;

    @Inject
    CarManager carManager;

    @Inject
    AccountManager accountManager;

    @Inject
    InstructorAccessManager instructorAccessManager;

    @Inject
    CourseFacade courseFacade;

    @RolesAllowed("createCourse")
    public void createCourse(CourseCategory courseCategory, TraineeAccess traineeAccess) throws BaseException {
        boolean hasOngoingCourse = traineeAccess.getCourses().stream()
                .anyMatch(x -> !x.isCourseCompletion());
        boolean hasSameCourseCompleted = traineeAccess.getCourses().stream()
                .anyMatch(x -> x.getCourseDetails().getCourseCategory().equals(courseCategory));

        if (hasOngoingCourse) {
            throw CourseException.alreadyAssigned();
        } else if (hasSameCourseCompleted) {
            throw CourseException.alreadyCompleted();
        }

        CourseDetails courseDetails = courseDetailsManager.findByCategory(courseCategory);
        Course course = new Course(traineeAccess, courseDetails);
        course.setCreatedBy(traineeAccess.getAccount());

        traineeAccess.getCourses().add(course);
        traineeAccess.setModificationDate(Date.from(Instant.now()));
        traineeAccess.setModifiedBy(traineeAccess.getAccount());

        courseDetails.getCourses().add(course);
        courseDetails.setModificationDate(Date.from(Instant.now()));
        courseDetails.setModifiedBy(traineeAccess.getAccount());

        traineeAccessManager.edit(traineeAccess);
        courseDetailsManager.edit(courseDetails);
    }

    @RolesAllowed(
            {"createPayment", "cancelPayment", "confirmPayment", "rejectPayment", "addDrivingLesson", "getOtherCourse"})
    public Course getOngoingCourse(String login) throws BaseException {
        Account account = accountManager.findByLogin(login);
        TraineeAccess traineeAccess = traineeAccessManager.find(account);
        List<Course> courses = courseFacade.findByTraineeId(traineeAccess.getId());

        return courses.stream()
                .filter(x -> !x.isCourseCompletion())
                .findAny()
                .orElseThrow(CourseException::noOngoingCourse);
    }

    @RolesAllowed({"createPayment", "assignToLectureGroup"})
    public Course findById(Long id) throws BaseException {
        return courseFacade.find(id);
    }

    @RolesAllowed({"createPayment", "cancelPayment", "confirmPayment", "rejectPayment", "addLectureForGroup",
            "addDrivingLesson", "cancelDrivingLesson"})
    public void edit(Course course) throws BaseException {
        courseFacade.edit(course);
    }

    @RolesAllowed("getUnderpayments")
    public List<Course> findByCategory(CourseCategory courseCategory) throws BaseException {
        return courseFacade.findByCategory(courseCategory);
    }

    @RolesAllowed("getTraineesForGroup")
    public List<Course> getPaidCoursesWithoutLectureGroup(CourseCategory courseCategory) throws BaseException {
        List<Course> courses = courseFacade.findByCategory(courseCategory);
        return courses.stream()
                .filter(x -> x.isAdvance() && x.getLectureGroup() == null)
                .collect(Collectors.toList());
    }

    @RolesAllowed("getBriefCourseInfo")
    public BriefCourseInfoDto getBriefCourseInfo(Course course) {
        String courseCategory = course.getCourseDetails().getCourseCategory().toString();
        BigDecimal price = course.getCourseDetails().getPrice();
        BigDecimal valueOfPayments = course.getPayments().stream()
                .filter(x -> x.getPaymentStatus().equals(PaymentStatus.CONFIRMED))
                .map(Payment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BriefCourseInfoDto(courseCategory, price, valueOfPayments);
    }

    @RolesAllowed("getCourseStatistics")
    public CourseStatisticsDto getCourseStatistics(Course course) throws BaseException {
        String instructorDetails = "";
        String carDetails = "";

        List<Lecture> lectures = course.getLectureGroup().getLectures().stream()
                .filter(x -> x.getDateFrom().before(Date.from(Instant.now())))
                .collect(Collectors.toList());
        long lecturesHours = countLectureHours(lectures);

        List<DrivingLesson> takenDrivingLessons = course.getDrivingLessons().stream()
                .filter(x -> x.getLessonStatus().equals(LessonStatus.FINISHED) ||
                        x.getLessonStatus().equals(LessonStatus.IN_PROGRESS))
                .collect(Collectors.toList());
        long takenDrivingLessonsHours = countDrivingHours(takenDrivingLessons);

        List<DrivingLesson> futureDrivingLessons = course.getDrivingLessons().stream()
                .filter(x -> x.getLessonStatus().equals(LessonStatus.PENDING))
                .collect(Collectors.toList());
        long futureDrivingLessonsHours = countDrivingHours(futureDrivingLessons);

        Map<String, Long> occurrenceOfInstructors = course.getDrivingLessons().stream()
                .filter(x -> !x.getLessonStatus().equals(LessonStatus.CANCELLED))
                .collect(Collectors.groupingBy(w -> w.getInstructor().getAccount().getLogin(), Collectors.counting()));
        if (occurrenceOfInstructors.size() > 0) {
            String favouriteInstructorLogin =
                    Collections.max(occurrenceOfInstructors.entrySet(), Map.Entry.comparingByValue()).getKey();
            Account favouriteInstructor = accountManager.findByLogin(favouriteInstructorLogin);
            instructorDetails =
                    favouriteInstructor.getFirstname().concat(" ").concat(favouriteInstructor.getLastname());
        }

        Map<Long, Long> occurrenceOfCars = course.getDrivingLessons().stream()
                .filter(x -> !x.getLessonStatus().equals(LessonStatus.CANCELLED))
                .collect(Collectors.groupingBy(w -> w.getCar().getId(), Collectors.counting()));
        if (occurrenceOfCars.size() > 0) {
            Long favouriteCarId = Collections.max(occurrenceOfCars.entrySet(), Map.Entry.comparingByValue()).getKey();
            Car favouriteCar = carManager.find(favouriteCarId);
            carDetails = favouriteCar.getBrand().concat(" ").concat(favouriteCar.getModel());
        }


        return new CourseStatisticsDto(lecturesHours, takenDrivingLessonsHours, futureDrivingLessonsHours,
                instructorDetails, carDetails);
    }

    @RolesAllowed("getInstructorStatistics")
    public InstructorStatisticsDto getInstructorStatistics(Long from, Long to) throws BaseException {
        List<Account> accounts = accountManager.getAllAccounts();
        List<String> instructors = new ArrayList<>();
        List<Long> numberOfHours = new ArrayList<>();

        for (Account account : accounts) {
            boolean isInstructor = account.getAccesses().stream()
                    .anyMatch(x -> x.getAccessType() == AccessType.INSTRUCTOR && x.isActivated());

            if (isInstructor) {
                InstructorAccess instructorAccess = findInstructorAccess(account);
                instructors.add(account.getFirstname().concat(" ").concat(account.getLastname()));
                List<DrivingLesson> filteredDrivingLessons =
                        filterDrivingLessons(instructorAccess.getDrivingLessons(), from, to);
                numberOfHours.add(countDrivingHours(filteredDrivingLessons));
            }
        }

        return new InstructorStatisticsDto(instructors, numberOfHours);
    }

    private long countLectureHours(List<Lecture> lectures) {
        long totalNumberOfHours = 0;

        for (Lecture lecture : lectures) {
            long diffInMillis = lecture.getDateTo().getTime() - lecture.getDateFrom().getTime();
            totalNumberOfHours += TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        }

        return totalNumberOfHours;
    }

    private long countDrivingHours(List<DrivingLesson> drivingLessons) {
        long totalNumberOfHours = 0;

        for (DrivingLesson drivingLesson : drivingLessons) {
            long diffInMillis = drivingLesson.getDateTo().getTime() - drivingLesson.getDateFrom().getTime();
            totalNumberOfHours += TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        }

        return totalNumberOfHours;
    }

    private InstructorAccess findInstructorAccess(Account account) throws BaseException {
        Access access = account.getAccesses().stream()
                .filter(x -> x.getAccessType() == AccessType.INSTRUCTOR)
                .findAny()
                .orElseThrow(AccessException::noProperAccess);

        return instructorAccessManager.find(account);
    }

    private List<DrivingLesson> filterDrivingLessons(Set<DrivingLesson> drivingLessons, Long from, Long to)
            throws BaseException {
        return drivingLessons.stream()
                .filter(x -> x.getDateFrom().getTime() >= from)
                .filter(x -> x.getDateTo().getTime() <= to)
                .collect(Collectors.toList());
    }
}
