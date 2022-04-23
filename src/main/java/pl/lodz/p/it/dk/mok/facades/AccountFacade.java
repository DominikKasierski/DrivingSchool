package pl.lodz.p.it.dk.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
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
    public Account findByLogin(String login) throws BaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findByLogin", Account.class);
            accountTypedQuery.setParameter("login", login);
            return accountTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @PermitAll
    public Account findByEmail(String emailAddress) throws BaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findByEmailAddress", Account.class);
            accountTypedQuery.setParameter("emailAddress", emailAddress);
            return accountTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e.getCause());
        } catch (PersistenceException e) {
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

    @Override
    @PermitAll
    public void remove(Account entity) throws BaseException {
        super.remove(entity);
    }

    @Override
    @PermitAll
    public List<Account> findAll() throws BaseException {
        return super.findAll();
    }

    @PermitAll
    public List<Account> findUnverifiedAccounts(Date expirationDate) throws BaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findUnverifiedAccounts", Account.class);
            accountTypedQuery.setParameter("date", expirationDate);
            return accountTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e);
        }
    }

    @RolesAllowed({"editOwnEmail", "editOtherEmail"})
    public boolean checkEmailOccurrence(String email) throws BaseException {
        try {
            Account account = findByEmail(email);
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }
}
