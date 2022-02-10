package pl.lodz.p.it.dk.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.ConfirmationCode;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.ConfirmationCodeException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ConfirmationCodeFacade extends AbstractFacade<ConfirmationCode> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfirmationCodeFacade() {
        super(ConfirmationCode.class);
    }

    @Override
    @PermitAll
    public void create(ConfirmationCode entity) throws BaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if(e.getCause().getMessage().contains(ConfirmationCode.CODE_CONSTRAINT)){
                throw ConfirmationCodeException.codeExists(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public void edit(ConfirmationCode entity) throws BaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if(e.getCause().getMessage().contains(ConfirmationCode.CODE_CONSTRAINT)){
                throw ConfirmationCodeException.codeExists(e.getCause());
            }
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @PermitAll
    public ConfirmationCode findByCode(String code) throws BaseException {
        try {
            TypedQuery<ConfirmationCode> query = em.createNamedQuery("ConfirmationCode.findByCode", ConfirmationCode.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.confirmationCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e);
        }
    }

    //TODO: Tutaj pewnie dojdzie sporo metod od scheduler'a
}
