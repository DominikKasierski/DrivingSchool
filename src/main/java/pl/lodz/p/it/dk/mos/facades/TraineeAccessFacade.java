package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.TraineeAccess;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class TraineeAccessFacade extends AbstractFacade<TraineeAccess> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TraineeAccessFacade() {
        super(TraineeAccess.class);
    }

    @Override
    @RolesAllowed("")
    public void create(TraineeAccess entity) throws BaseException {
        super.create(entity);
    }

    @Override
    @RolesAllowed("createCourse")
    public void edit(TraineeAccess entity) throws BaseException {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public TraineeAccess find(Object id) throws BaseException {
        return super.find(id);
    }

    @Override
    @PermitAll
    public List<TraineeAccess> findAll() throws BaseException {
        return super.findAll();
    }
}
