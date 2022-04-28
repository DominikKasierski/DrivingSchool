package pl.lodz.p.it.dk.mos.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Course;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.CourseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourseFacade extends AbstractFacade<Course> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseFacade() {
        super(Course.class);
    }

    @Override
    @RolesAllowed("createCourse")
    public void create(Course entity) throws BaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Course.TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT)) {
                throw CourseException.alreadyAssigned(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @RolesAllowed({""})
    public void edit(Course entity) throws BaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Course.TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT)) {
                throw CourseException.alreadyAssigned(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public Course find(Object id) throws BaseException {
        return super.find(id);
    }

    @RolesAllowed({""})
    public Course findByCategory(CourseCategory courseCategory) throws BaseException {
        try {
            TypedQuery<Course>
                    courseTypedQuery = em.createNamedQuery("Course.findByCategory", Course.class);
            courseTypedQuery.setParameter("category", courseCategory);
            return courseTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.courseNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({"getOwnCourse", "getOtherCourse"})
    public Course findByTraineeId(long traineeId) throws BaseException {
        try {
            TypedQuery<Course>
                    courseTypedQuery = em.createNamedQuery("Course.findByTraineeId", Course.class);
            courseTypedQuery.setParameter("traineeId", traineeId);
            return courseTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.courseNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public Course findByLectureGroupId(long lectureGroupId) throws BaseException {
        try {
            TypedQuery<Course>
                    courseTypedQuery = em.createNamedQuery("Course.findByLectureGroupId", Course.class);
            courseTypedQuery.setParameter("lectureGroupId", lectureGroupId);
            return courseTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.courseNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }
}
