package pl.lodz.p.it.dk.security.etag;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import pl.lodz.p.it.dk.common.configs.AppConfig;
import pl.lodz.p.it.dk.exceptions.AppRuntimeException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Signer {

    @Inject
    private AppConfig appConfig;

    private JWSSigner jwsSigner;

    @PostConstruct
    public void init() {
        try {
            jwsSigner = new MACSigner(appConfig.getEtagSecretKey());
        } catch (KeyLengthException e) {
            throw AppRuntimeException.etagException(e);
        }
    }

    public String sign(EntityToSign entity) {
        try {
            JWSObject jwsObject =
                    new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(entity.getMessageToSign()));
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw AppRuntimeException.etagException(e);
        }
    }

}
