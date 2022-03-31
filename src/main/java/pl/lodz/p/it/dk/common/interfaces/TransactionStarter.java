package pl.lodz.p.it.dk.common.interfaces;

public interface TransactionStarter {

    String getTransactionId();

    boolean isLastTransactionRollback();
}
