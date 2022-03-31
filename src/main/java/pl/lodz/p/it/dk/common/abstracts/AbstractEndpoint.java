package pl.lodz.p.it.dk.common.abstracts;

import lombok.Getter;
import lombok.extern.java.Log;
import pl.lodz.p.it.dk.security.etag.EntityToSign;
import pl.lodz.p.it.dk.security.etag.Signer;

import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.security.Principal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

@Log
public abstract class AbstractEndpoint {

    @Context
    private HttpHeaders httpHeaders;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Signer signer;

    @Getter
    private String transactionId;

    @Getter
    private boolean lastTransactionRollback;

    public boolean verifyIntegrity(EntityToSign signable) {
        String headerValue = httpHeaders.getRequestHeader("If-Match").get(0);
        String signerValue = signer.sign(signable);
        return signerValue.equals(headerValue);
    }

    @AfterBegin
    public void afterBegin() {
        transactionId =
                Long.toString(System.currentTimeMillis()) + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        log.log(Level.INFO, "Transaction with ID: {0} started in {1}, account: {2}",
                new Object[]{transactionId, this.getClass().getName(), getLogin()});
    }

    @AfterCompletion
    public void afterCompletion(boolean committed) {
        lastTransactionRollback = !committed;
        log.log(Level.INFO, "Transaction with ID: {0} ended in {1}, account: {2}, result: {3}",
                new Object[]{transactionId, this.getClass().getName(), getLogin(), getResult()});
    }

    protected String getLogin() {
        Principal principal = securityContext.getCallerPrincipal();
        return principal != null ? principal.getName() : "Guest";
    }

    private String getResult() {
        return lastTransactionRollback ? "rollback" : "commit";
    }
}
