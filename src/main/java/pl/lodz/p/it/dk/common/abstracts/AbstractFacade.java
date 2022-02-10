package pl.lodz.p.it.dk.common.abstracts;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
public abstract class AbstractFacade<T extends AbstractEntity> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected void create(T entity) throws BaseException {
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    protected void edit(T entity) throws BaseException {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppOptimisticLockException.optimisticLockException(e);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    //TODO: Czy będzie potrzebne?
    protected void remove(T entity) throws BaseException {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    protected T find(Object id) throws BaseException {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    protected List<T> findAll() throws BaseException {
        try {
            CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultStream().sorted(Comparator.comparing(T::getId).reversed()).collect(
                    Collectors.toList());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }
}
