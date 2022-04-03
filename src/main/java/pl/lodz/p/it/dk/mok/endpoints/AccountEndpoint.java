package pl.lodz.p.it.dk.mok.endpoints;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.managers.AccountManager;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Log
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint extends AbstractEndpoint implements AccountEndpointLocal {

    @Inject
    private AccountManager accountManager;

    @Override
    @PermitAll
    public void updateAuthInfo(String login, String language) throws BaseException {
        accountManager.updateAuthInfo(login, language);
    }

    @Override
    @PermitAll
    public void updateAuthInfo(String login) throws BaseException {
        accountManager.updateAuthInfo(login);
    }
}
