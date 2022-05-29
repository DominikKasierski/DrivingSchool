package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Car;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
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
public class CarFacade extends AbstractFacade<Car> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CarFacade() {
        super(Car.class);
    }

    @Override
    @RolesAllowed("addCar")
    public void create(Car entity) throws BaseException {
        super.create(entity);
    }

    @Override
    @RolesAllowed({"editCar", "addDrivingLesson"})
    public void edit(Car entity) throws BaseException {
        super.edit(entity);
    }

    @Override
    @RolesAllowed("editCar")
    public Car find(Object id) throws BaseException {
        return super.find(id);
    }

    @Override
    @PermitAll
    public List<Car> findAll() throws BaseException {
        return super.findAll();
    }

    @RolesAllowed({"addDrivingLesson"})
    public List<Car> findByCategory(CourseCategory courseCategory) throws BaseException {
        try {
            TypedQuery<Car>
                    carTypedQuery = em.createNamedQuery("Car.findByCategory", Car.class);
            carTypedQuery.setParameter("category", courseCategory);
            return carTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.carNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }
}
