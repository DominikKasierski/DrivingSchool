package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.AccessMapper;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mok.dtos.TraineeAccessDto;
import pl.lodz.p.it.dk.mos.dtos.CourseDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;
import pl.lodz.p.it.dk.mos.managers.AccountManager;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.TraineeAccessManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
public class CourseEndpoint extends AbstractEndpoint implements CourseEndpointLocal {

    @Inject
    AccountManager accountManager;

    @Inject
    TraineeAccessManager traineeAccessManager;

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
    @RolesAllowed("getTraineeForGroup")
    public List<TraineeForGroupDto> getTraineesForGroup(CourseCategory courseCategory) throws BaseException {
        List<Course> coursesForGroup = courseManager.getCoursesForGroup(courseCategory);
        List<TraineeForGroupDto> traineesForGroup = new ArrayList<>();

        for (Course course : coursesForGroup) {
            traineesForGroup.add(Mappers.getMapper(CourseMapper.class).toTraineeForGroup(course));
        }

        return traineesForGroup;
    }
}
