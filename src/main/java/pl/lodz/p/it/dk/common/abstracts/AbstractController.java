package pl.lodz.p.it.dk.common.abstracts;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.common.interfaces.ReturnMethodExecutor;
import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.common.interfaces.VoidMethodExecutor;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;
import pl.lodz.p.it.dk.exceptions.TransactionException;

import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.util.logging.Level;

@Log
public abstract class AbstractController {

    @Context
    ServletContext servletContext;

    protected void repeat(VoidMethodExecutor executor, TransactionStarter transactionStarter) throws BaseException {
        int repeatTransactionLimit = Integer.parseInt(servletContext.getInitParameter("repeatTransactionLimit"));
        int callCounter = 0;
        boolean rollback;

        do {
            try {
                executor.execute();
                rollback = transactionStarter.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                rollback = true;
            }

            if (callCounter > 0) {
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{transactionStarter.getTransactionId(), callCounter});
            }
            callCounter++;
        } while (rollback && callCounter <= repeatTransactionLimit);

        if (callCounter > repeatTransactionLimit) {
            throw TransactionException.limitExceeded();
        }
    }

    protected <T> T repeat(ReturnMethodExecutor<T> executor, TransactionStarter transactionStarter)
            throws BaseException {
        int repeatTransactionLimit = Integer.parseInt(servletContext.getInitParameter("repeatTransactionLimit"));
        int callCounter = 0;
        boolean rollback;
        T result = null;

        do {
            try {
                result = executor.execute();
                rollback = transactionStarter.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                rollback = true;
            }
            if (callCounter > 0) {
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{transactionStarter.getTransactionId(), callCounter});
            }
            callCounter++;
        } while (rollback && callCounter <= repeatTransactionLimit);

        if (callCounter > repeatTransactionLimit) {
            throw TransactionException.limitExceeded();
        }
        return result;
    }
}
