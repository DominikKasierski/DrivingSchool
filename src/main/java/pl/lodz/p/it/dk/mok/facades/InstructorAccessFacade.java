package pl.lodz.p.it.dk.mok.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.InstructorAccess;
import pl.lodz.p.it.dk.exceptions.BaseException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class InstructorAccessFacade extends AbstractFacade<InstructorAccess> {

    @PersistenceContext(unitName = "mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstructorAccessFacade() {
        super(InstructorAccess.class);
    }

    @Override
    @PermitAll
    public InstructorAccess find(Object id) throws BaseException {
        return super.find(id);
    }

    @Override
    @RolesAllowed("addPermissionCategory")
    public void edit(InstructorAccess entity) throws BaseException {
        super.edit(entity);
    }
}
