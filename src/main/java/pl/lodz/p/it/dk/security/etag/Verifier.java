package pl.lodz.p.it.dk.security.etag;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import pl.lodz.p.it.dk.common.configs.AppConfig;
import pl.lodz.p.it.dk.exceptions.AppRuntimeException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;

@ApplicationScoped
public class Verifier {

    @Inject
    private AppConfig appConfig;

    private JWSVerifier jwsVerifier;

    @PostConstruct
    public void init() {
        try {
            jwsVerifier = new MACVerifier(appConfig.getEtagSecretKey());
        } catch (JOSEException e) {
            throw AppRuntimeException.etagException(e);
        }
    }

    public boolean verify(String etag) {
        try {
            final JWSObject jwsObject = JWSObject.parse(etag);
            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            throw AppRuntimeException.etagException(e);
        }
    }

}
