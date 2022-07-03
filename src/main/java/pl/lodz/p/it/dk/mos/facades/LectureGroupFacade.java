package pl.lodz.p.it.dk.mos.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.LectureGroupException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class LectureGroupFacade extends AbstractFacade<LectureGroup> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LectureGroupFacade() {
        super(LectureGroup.class);
    }

    @Override
    @RolesAllowed("createLectureGroup")
    public void create(LectureGroup entity) throws BaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(LectureGroup.NAME_CONSTRAINT)) {
                throw LectureGroupException.nameExists(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({"addLectureForGroup"})
    @Override
    public void edit(LectureGroup entity) throws BaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(LectureGroup.NAME_CONSTRAINT)) {
                throw LectureGroupException.nameExists(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public LectureGroup find(Object id) throws BaseException {
        return super.find(id);
    }

    @Override
    @PermitAll
    public List<LectureGroup> findAll() throws BaseException {
        return super.findAll();
    }

    @PermitAll
    public LectureGroup findByName(String name) throws BaseException {
        try {
            TypedQuery<LectureGroup> lectureGroupTypedQuery =
                    em.createNamedQuery("LectureGroup.findByName", LectureGroup.class);
            lectureGroupTypedQuery.setParameter("name", name);
            return lectureGroupTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.lectureGroupNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed({""})
    public List<LectureGroup> findByCategory(CourseCategory courseCategory) throws BaseException {
        try {
            TypedQuery<LectureGroup>
                    lectureGroupTypedQuery = em.createNamedQuery("LectureGroup.findByCategory", LectureGroup.class);
            lectureGroupTypedQuery.setParameter("category", courseCategory);
            return lectureGroupTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.lectureGroupNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    //TODO: findByTraineeId?
}
