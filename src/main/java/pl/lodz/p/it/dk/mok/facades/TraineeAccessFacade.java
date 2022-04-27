package pl.lodz.p.it.dk.mok.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TraineeAccessFacade extends AbstractFacade<TraineeAccess> {

    @PersistenceContext(unitName = "mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TraineeAccessFacade() {
        super(TraineeAccess.class);
    }

    @Override
    @PermitAll
    public TraineeAccess find(Object id) throws BaseException {
        return super.find(id);
    }
}
