package pl.lodz.p.it.dk.auth.endpoints;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.auth.dtos.LoginDataDto;
import pl.lodz.p.it.dk.common.abstracts.AbstractEndpoint;
import pl.lodz.p.it.dk.exceptions.AuthException;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.security.JwtUtils;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

@Log
@Stateful
public class AuthEndpoint extends AbstractEndpoint implements AuthEndpointLocal {

    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public String login(LoginDataDto loginDataDto) throws BaseException {
        Credential credential = new UsernamePasswordCredential(loginDataDto.getLogin(), loginDataDto.getPassword());
        CredentialValidationResult validationResult = identityStoreHandler.validate(credential);
        if (validationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            log.info(String.format("User %s has logged in.", getLogin()));
            return jwtUtils.generate(validationResult);
        } else {
            throw AuthException.invalidCredentials();
        }
    }

    @Override
    public void logout() {
        log.info(String.format("User %s has logged out.", getLogin()));
    }

    @Override
    public String updateToken(String currentToken) {
        return jwtUtils.update(currentToken);
    }
}
