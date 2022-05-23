package pl.lodz.p.it.dk.mos.facades;

import pl.lodz.p.it.dk.common.abstracts.AbstractFacade;
import pl.lodz.p.it.dk.entities.Payment;
import pl.lodz.p.it.dk.entities.enums.PaymentStatus;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.NotFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PaymentFacade extends AbstractFacade<Payment> {

    @PersistenceContext(unitName = "mosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentFacade() {
        super(Payment.class);
    }

    @Override
    @RolesAllowed("")
    public void create(Payment entity) throws BaseException {
        super.create(entity);
    }

    @Override
    @RolesAllowed({""})
    public void edit(Payment entity) throws BaseException {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public Payment find(Object id) throws BaseException {
        return super.find(id);
    }

    @RolesAllowed("")
    public List<Payment> findByTraineeId(String traineeId) throws BaseException {
        try {
            TypedQuery<Payment> paymentTypedQuery =
                    em.createNamedQuery("Payment.findByTraineeId", Payment.class);
            paymentTypedQuery.setParameter("traineeId", traineeId);
            return paymentTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed("getPaymentsForApproval")
    public List<Payment> findByStatus(PaymentStatus paymentStatus) throws BaseException {
        try {
            TypedQuery<Payment> paymentTypedQuery =
                    em.createNamedQuery("Payment.findByStatus", Payment.class);
            paymentTypedQuery.setParameter("status", paymentStatus);
            return paymentTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

    @RolesAllowed("generateReport")
    public List<Payment> findAllPaymentsInTimeRange(Date from, Date to) throws BaseException {
        try {
            TypedQuery<Payment> paymentQuery = em.createNamedQuery("Payment.findAllInRange", Payment.class);
            paymentQuery.setParameter("from", from);
            paymentQuery.setParameter("to", to);
            return paymentQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
    }

}
