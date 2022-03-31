package pl.lodz.p.it.dk.common.abstracts;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.common.configs.AppConfig;
import pl.lodz.p.it.dk.common.interfaces.CallingClass;
import pl.lodz.p.it.dk.common.interfaces.ReturnMethodExecutor;
import pl.lodz.p.it.dk.common.interfaces.VoidMethodExecutor;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.DatabaseException;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.util.logging.Level;

//  TODO: Sprawdzać czy callCounter > repeatTransactionLimit ?

@Log
public abstract class AbstractController {

    @Context
    ServletContext servletContext;

    @Inject
    private AppConfig appConfig;

    protected void repeat(VoidMethodExecutor executor, CallingClass callingClass) throws BaseException {
        int repeatTransactionLimit = appConfig.getEjbRepeatTransactionLimit();
        int callCounter = 0;
        boolean rollback;

        do {
            try {
                executor.execute();
                rollback = callingClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                rollback = true;
            }

            if (callCounter > 0) {
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{callingClass.getTransactionId(), callCounter});
            }
            callCounter++;
        } while (rollback && callCounter <= repeatTransactionLimit);
    }

    protected <T> T repeat(ReturnMethodExecutor<T> executor, CallingClass callingClass) throws BaseException {
        int repeatTransactionLimit = appConfig.getEjbRepeatTransactionLimit();
        int callCounter = 0;
        boolean rollback;
        T result = null;

        do {
            try {
                result = executor.execute();
                rollback = callingClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                rollback = true;
            }
            if (callCounter > 0) {
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{callingClass.getTransactionId(), callCounter});
            }
            callCounter++;
        } while (rollback && callCounter <= repeatTransactionLimit);

        return result;
    }
}
