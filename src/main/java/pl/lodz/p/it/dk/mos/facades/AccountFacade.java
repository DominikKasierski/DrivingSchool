package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
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
}
