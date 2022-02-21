package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.LectureGroup;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
}
