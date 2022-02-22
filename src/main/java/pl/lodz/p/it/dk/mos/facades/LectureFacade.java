package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Lecture;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class LectureFacade extends AbstractFacade<Lecture> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LectureFacade() {
        super(Lecture.class);
    }

    @Override
    @RolesAllowed("")
    public void create(Lecture entity) throws BaseException {
        super.create(entity);
    }

    @Override
    @RolesAllowed({""})
    public void edit(Lecture entity) throws BaseException {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public Lecture find(Object id) throws BaseException {
        return super.find(id);
    }

    @RolesAllowed({""})
    public List<Lecture> findByInstructorId(long instructorId) throws BaseException {
        try {
            TypedQuery<Lecture>
                    lectureTypedQuery = em.createNamedQuery("Lecture.findByInstructorId", Lecture.class);
            lectureTypedQuery.setParameter("instructorId", instructorId);
            return lectureTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.courseNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<Lecture> findByLectureGroupId(long lectureGroupId) throws BaseException {
        try {
            TypedQuery<Lecture>
                    lectureTypedQuery = em.createNamedQuery("Lecture.findByLectureGroupId", Lecture.class);
            lectureTypedQuery.setParameter("lectureGroupId", lectureGroupId);
            return lectureTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.courseNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }
}
