package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.DrivingLesson;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DrivingLessonFacade extends AbstractFacade<DrivingLesson> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DrivingLessonFacade() {
        super(DrivingLesson.class);
    }

    @Override
    @RolesAllowed("")
    public void create(DrivingLesson entity) throws BaseException {
        super.create(entity);
    }

    @Override
    @PermitAll
    public void edit(DrivingLesson entity) throws BaseException {
        super.edit(entity);
    }

    @Override
    @RolesAllowed("cancelDrivingLesson")
    public DrivingLesson find(Object id) throws BaseException {
        return super.find(id);
    }

    @PermitAll
    public List<DrivingLesson> findStartedLessons(Date date) throws BaseException {
        try {
            TypedQuery<DrivingLesson> drivingLessonQuery =
                    em.createNamedQuery("DrivingLesson.findStartedLessons", DrivingLesson.class);
            drivingLessonQuery.setParameter("date", date);
            return drivingLessonQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @PermitAll
    public List<DrivingLesson> findFinishedLessons(Date date) throws BaseException {
        try {
            TypedQuery<DrivingLesson> drivingLessonQuery =
                    em.createNamedQuery("DrivingLesson.findFinishedLessons", DrivingLesson.class);
            drivingLessonQuery.setParameter("date", date);
            return drivingLessonQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<DrivingLesson> findAllActive() throws BaseException {
        try {
            TypedQuery<DrivingLesson>
                    drivingLessonTypedQuery = em.createNamedQuery("DrivingLesson.findAllActive", DrivingLesson.class);
            return drivingLessonTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<DrivingLesson> findAllArchive() throws BaseException {
        try {
            TypedQuery<DrivingLesson>
                    drivingLessonTypedQuery = em.createNamedQuery("DrivingLesson.findAllArchive", DrivingLesson.class);
            return drivingLessonTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<DrivingLesson> findByTraineeId(long traineeId) throws BaseException {
        try {
            TypedQuery<DrivingLesson>
                    drivingLessonTypedQuery = em.createNamedQuery("DrivingLesson.findByTraineeId", DrivingLesson.class);
            drivingLessonTypedQuery.setParameter("traineeId", traineeId);
            return drivingLessonTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<DrivingLesson> findByInstructorId(long instructorId) throws BaseException {
        try {
            TypedQuery<DrivingLesson>
                    drivingLessonTypedQuery =
                    em.createNamedQuery("DrivingLesson.findByInstructorId", DrivingLesson.class);
            drivingLessonTypedQuery.setParameter("instructorId", instructorId);
            return drivingLessonTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }


}
