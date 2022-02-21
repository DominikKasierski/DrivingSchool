package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Course;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    //TODO: znajdź opłacone (ale kurs nieukończony), znajdź z zakończonymi wykładami (ale kurs nieukończony), znajdź z niezakończonymi wykładami, znajdź ukończone, find, edit, create

}
