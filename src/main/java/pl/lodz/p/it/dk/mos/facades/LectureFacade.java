package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Lecture;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
