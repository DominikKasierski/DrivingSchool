package pl.lodz.p.it.dk.mos.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.CourseDetails;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.CourseDetailsException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CourseDetailsFacade extends AbstractFacade<CourseDetails> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseDetailsFacade() {
        super(CourseDetails.class);
    }

    @Override
    @PermitAll
    public void edit(CourseDetails entity) throws BaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(CourseDetails.COURSE_CATEGORY_CONSTRAINT)) {
                throw CourseDetailsException.categoryExists(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public CourseDetails find(Object id) throws BaseException {
        return super.find(id);
    }

    @PermitAll
    public CourseDetails findByCategory(CourseCategory courseCategory) throws BaseException {
        try {
            TypedQuery<CourseDetails>
                    courseDetailsTypedQuery = em.createNamedQuery("CourseDetails.findByCategory", CourseDetails.class);
            courseDetailsTypedQuery.setParameter("category", courseCategory);
            return courseDetailsTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.courseDetailsNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }
}
