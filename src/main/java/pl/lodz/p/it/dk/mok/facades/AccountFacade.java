package pl.lodz.p.it.dk.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.AccessException;
import pl.lodz.p.it.dk.exceptions.AccountException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    @PermitAll
    public void create(Account entity) throws BaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            handleConstraintViolationException(e);
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public void edit(Account entity) throws BaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            handleConstraintViolationException(e);
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @PermitAll
    private void handleConstraintViolationException(ConstraintViolationException e)
            throws BaseException {
        if (e.getCause().getMessage().contains(Account.LOGIN_CONSTRAINT)) {
            throw AccountException.loginExists(e.getCause());
        } else if (e.getCause().getMessage().contains(Account.EMAIL_ADDRESS_CONSTRAINT)) {
            throw AccountException.emailExists(e.getCause());
        } else if (e.getCause().getMessage().contains(Account.PHONE_NUMBER_CONSTRAINT)) {
            throw AccountException.phoneNumberExists(e.getCause());
        } else if (e.getCause().getMessage().contains(Access.ACCESS_TYPE_ACCOUNT_ID_CONSTRAINT)) {
            throw AccessException.alreadyGranted(e.getCause());
        }
    }

    @PermitAll
    @Override
    public void remove(Account entity) throws BaseException {
        super.remove(entity);
    }

    @DenyAll
    @Override
    public Account find(Object id) throws BaseException {
        return super.find(id);
    }

    @Override
    @PermitAll
    public List<Account> findAll() throws BaseException {
        return super.findAll();
    }

}
