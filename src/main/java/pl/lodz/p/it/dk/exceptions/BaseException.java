package pl.lodz.p.it.dk.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BaseException extends Exception {

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
