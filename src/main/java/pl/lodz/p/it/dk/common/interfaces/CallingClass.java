package pl.lodz.p.it.dk.common.interfaces;

public interface CallingClass {

    String getTransactionId();

    boolean isLastTransactionRollback();
}
