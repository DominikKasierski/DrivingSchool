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

    @RolesAllowed("createPayment")
    public List<Payment> findByCourseId(Long courseId) throws BaseException {
        try {
            TypedQuery<Payment> paymentTypedQuery =
                    em.createNamedQuery("Payment.findByCourseId", Payment.class);
            paymentTypedQuery.setParameter("courseId", courseId);
            return paymentTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.paymentNotFound(e.getCause());
        } catch (PersistenceException e) {
            throw DatabaseException.queryException(e.getCause());
        }
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

    @RolesAllowed("")
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
}
