package pl.lodz.p.it.dk.mos.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mappers.CourseMapper;
import pl.lodz.p.it.dk.mappers.LectureGroupMapper;
import pl.lodz.p.it.dk.mos.dtos.NewLectureGroupDto;
import pl.lodz.p.it.dk.mos.dtos.TraineeForGroupDto;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.LectureGroupManager;

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
public class LectureGroupEndpoint extends AbstractEndpoint implements LectureGroupEndpointLocal {

    @Inject
    CourseManager courseManager;

    @Inject
    LectureGroupManager lectureGroupManager;

    @Override
    @RolesAllowed("getTraineeForGroup")
    public List<TraineeForGroupDto> getTraineesForGroup(CourseCategory courseCategory) throws BaseException {
        List<Course> coursesForGroup = courseManager.getPaidCoursesWithoutLectureGroup(courseCategory);
        List<TraineeForGroupDto> traineesForGroup = new ArrayList<>();

        for (Course course : coursesForGroup) {
            traineesForGroup.add(Mappers.getMapper(CourseMapper.class).toTraineeForGroup(course));
        }

        return traineesForGroup;
    }

    @Override
    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(NewLectureGroupDto newLectureGroupDto) throws BaseException {
        LectureGroup lectureGroup = new LectureGroup();
        Mappers.getMapper(LectureGroupMapper.class).toLectureGroup(newLectureGroupDto, lectureGroup);
        lectureGroupManager.createLectureGroup(lectureGroup, getLogin());
    }
}