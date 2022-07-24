package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.InstructorAccess;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.AccessMapper;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;
import pl.lodz.p.it.dk.mos.dtos.*;
import pl.lodz.p.it.dk.mos.managers.AccountManager;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.InstructorAccessManager;
import pl.lodz.p.it.dk.mos.managers.TraineeAccessManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CourseEndpoint extends AbstractEndpoint implements CourseEndpointLocal {

    @Inject
    AccountManager accountManager;

    @Inject
    TraineeAccessManager traineeAccessManager;

    @Inject
    InstructorAccessManager instructorAccessManager;

    @Inject
    CourseManager courseManager;

    @Override
    @RolesAllowed("createCourse")
    public void createCourse(CourseCategory courseCategory) throws BaseException {
        Account account = accountManager.findByLogin(getLogin());
        TraineeAccess traineeAccess = traineeAccessManager.find(account);
        TraineeAccessDto traineeAccessDto = Mappers.getMapper(AccessMapper.class).toTraineeAccessDto(traineeAccess);
        verifyEntityIntegrity(traineeAccessDto);
        courseManager.createCourse(courseCategory, traineeAccess);
    }

    @Override
    @RolesAllowed("getOwnCourse")
    public CourseDto getOwnCourse() throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        return Mappers.getMapper(CourseMapper.class).toCourseDto(course);
    }

    @Override
    @RolesAllowed("getOtherCourse")
    public CourseDto getOtherCourse(String login) throws BaseException {
        Course course = courseManager.getOngoingCourse(login);
        return Mappers.getMapper(CourseMapper.class).toCourseDto(course);
    }

    @Override
    @RolesAllowed("getBriefCourseInfo")
    public BriefCourseInfoDto getBriefCourseInfo() throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        return courseManager.getBriefCourseInfo(course);
    }

    @Override
    @RolesAllowed("getCourseStatistics")
    public CourseStatisticsDto getCourseStatistics() throws BaseException {
        Course course = courseManager.getOngoingCourse(getLogin());
        return courseManager.getCourseStatistics(course);
    }

    @Override
    @RolesAllowed("getInstructorStatistics")
    public InstructorStatisticsDto getInstructorStatistics(Long from, Long to) throws BaseException {
        return courseManager.getInstructorStatistics(from, to);
    }

    @Override
    @RolesAllowed("getCalendar")
    public CalendarDto getCalendar(String login, Long from, Boolean trainee) throws BaseException {
        if (trainee) {
            Course course = courseManager.getOngoingCourse(login);
            return courseManager.getCalendar(course.getLectureGroup().getLectures(), course.getDrivingLessons(), from,
                    true);
        } else {
            Account account = accountManager.findByLogin(getLogin());
            InstructorAccess instructorAccess = instructorAccessManager.find(account);
            return courseManager.getCalendar(instructorAccess.getLectures(), instructorAccess.getDrivingLessons(), from,
                    false);
        }
    }
}
