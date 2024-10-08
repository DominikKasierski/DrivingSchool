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
import pl.lodz.p.it.dk.mos.dtos.*;
import pl.lodz.p.it.dk.mos.managers.CourseManager;
import pl.lodz.p.it.dk.mos.managers.LectureGroupManager;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
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
    @RolesAllowed("getTraineesForGroup")
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

    @RolesAllowed("getLectureGroups")
    public List<LectureGroupDto> getLectureGroups() throws BaseException {
        List<LectureGroup> lectureGroups = lectureGroupManager.getOpenLectureGroups();
        List<LectureGroupDto> lectureGroupsDto = new ArrayList<>();

        for (LectureGroup lectureGroup : lectureGroups) {
            lectureGroupsDto.add(Mappers.getMapper(LectureGroupMapper.class)
                    .toLectureGroupDto(lectureGroup, lectureGroup.getCourses().size()));
        }

        return lectureGroupsDto;
    }

    @Override
    @RolesAllowed("getLectureGroup")
    public LectureGroupDto getLectureGroup(Long id) throws BaseException {
        LectureGroup lectureGroup = lectureGroupManager.findById(id);
        return Mappers.getMapper(LectureGroupMapper.class)
                .toLectureGroupDto(lectureGroup, lectureGroup.getCourses().size());
    }

    @Override
    @RolesAllowed("assignToLectureGroup")
    public void assignToLectureGroup(Long lectureGroupId, Long courseId) throws BaseException {
        LectureGroup lectureGroup = lectureGroupManager.findById(lectureGroupId);
        LectureGroupDto lectureGroupDto = Mappers.getMapper(LectureGroupMapper.class)
                .toLectureGroupDto(lectureGroup, lectureGroup.getCourses().size());
        verifyEntityIntegrity(lectureGroupDto);
        lectureGroupManager.assignToLectureGroup(lectureGroup, courseId, getLogin());
    }

    @Override
    @RolesAllowed("addLectureForGroup")
    public void addLectureForGroup(@NotNull @Valid NewLectureDto newLectureDto) throws BaseException {
        LectureGroup lectureGroup = lectureGroupManager.findById(newLectureDto.getLectureGroupId());
        LectureGroupDto lectureGroupDto = Mappers.getMapper(LectureGroupMapper.class)
                .toLectureGroupDto(lectureGroup, lectureGroup.getCourses().size());
        verifyEntityIntegrity(lectureGroupDto);
        lectureGroupManager.addLectureForGroup(lectureGroup, new Date(newLectureDto.getDateFrom() * 1000),
                new Date(newLectureDto.getDateTo() * 1000), newLectureDto.getInstructorLogin(), getLogin());
    }

    @Override
    @RolesAllowed("getGroupCalendar")
    public CalendarDto getGroupCalendar(Long lectureGroupId, Long from) throws BaseException {
        LectureGroup lectureGroup = lectureGroupManager.findById(lectureGroupId);
        return lectureGroupManager.getGroupCalendar(lectureGroup, from);
    }

}