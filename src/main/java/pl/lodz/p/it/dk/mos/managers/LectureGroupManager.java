package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.LectureGroupException;
import pl.lodz.p.it.dk.mos.facades.LectureGroupFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class LectureGroupManager {

    @Inject
    AccountManager accountManager;

    @Inject
    CourseManager courseManager;

    @Inject
    LectureGroupFacade lectureGroupFacade;

    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(LectureGroup lectureGroup, String login) throws BaseException {
        lectureGroup.setCreatedBy(accountManager.findByLogin(login));
        lectureGroupFacade.create(lectureGroup);
    }

    @RolesAllowed("getLectureGroups")
    public List<LectureGroup> getOpenLectureGroups() throws BaseException {
        List<LectureGroup> lectureGroups = lectureGroupFacade.findAll();
        List<LectureGroup> openLectureGroups = new ArrayList<>();

        for (LectureGroup lectureGroup : lectureGroups) {
            if (lectureGroup.getCourses().isEmpty() ||
                    !lectureGroup.getCourses().iterator().next().isLecturesCompletion()) {
                openLectureGroups.add(lectureGroup);
            }
        }

        return openLectureGroups;
    }

    @RolesAllowed("getLectureGroup")
    public LectureGroup findById(Long id) throws BaseException {
        return lectureGroupFacade.find(id);
    }

    @RolesAllowed("assignToLectureGroup")
    public void assignToLectureGroup(LectureGroup lectureGroup, Long courseId, String login) throws BaseException {
        Course course = courseManager.findById(courseId);

        if (course.getLectureGroup() != null) {
            throw LectureGroupException.alreadyAssigned();
        } else if (!course.isPaid()) {
            throw LectureGroupException.unpaidCourse();
        } else if (!lectureGroup.getLectures().isEmpty()) {
            throw LectureGroupException.lecturesStarted();
        }

        course.setLectureGroup(lectureGroup);
        course.setModificationDate(Date.from(Instant.now()));
        course.setModifiedBy(accountManager.findByLogin(login));

        lectureGroup.getCourses().add(course);
        lectureGroup.setModificationDate(Date.from(Instant.now()));
        lectureGroup.setModifiedBy(accountManager.findByLogin(login));
        lectureGroupFacade.edit(lectureGroup);
    }

}
