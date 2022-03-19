package pl.lodz.p.it.dk.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import pl.lodz.p.it.dk.common.configs.AppConfig;
import pl.lodz.p.it.dk.exceptions.AppRuntimeException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.text.ParseException;
import java.util.Date;

@RequestScoped
public class JwtUtils {

    @Inject
    private AppConfig appConfig;

    private static String JWT_SECRET_KEY;
    private static long JWT_EXPIRE_TIMEOUT;
    private static String JWT_ISSUER;

    @PostConstruct
    private void init() {
        JWT_SECRET_KEY = appConfig.getJwtSecretKey();
        JWT_EXPIRE_TIMEOUT = appConfig.getJwtExpireTimeout();
        JWT_ISSUER = appConfig.getJwtIssuer();
    }

    public String generate(CredentialValidationResult result) {
        try {
            JWSSigner signer = new MACSigner(JWT_SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

            String subject = result.getCallerPrincipal().getName();
            String roles = String.join(",", result.getCallerGroups());
            JWTClaimsSet claimsSet = getClaimsSet(subject, roles);

            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    public String update(String tokenToUpdate) {
        try {
            JWSSigner signer = new MACSigner(JWT_SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
            JWTClaimsSet oldClaimSet = SignedJWT.parse(tokenToUpdate).getJWTClaimsSet();

            String subject = oldClaimSet.getSubject();
            Object roles = oldClaimSet.getClaim("roles");
            JWTClaimsSet newClaimsSet = getClaimsSet(subject, roles);

            SignedJWT newSignedJWT = new SignedJWT(jwsHeader, newClaimsSet);
            newSignedJWT.sign(signer);
            return newSignedJWT.serialize();
        } catch (ParseException | JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    public boolean verify(String tokenToVerify) {
        try {
            JWSObject jwsObject = JWSObject.parse(tokenToVerify);
            JWSVerifier jwsVerifier = new MACVerifier(JWT_SECRET_KEY);
            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    private JWTClaimsSet getClaimsSet(String subject, Object roles) {
        return new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("roles", roles)
                .expirationTime(new Date(new Date().getTime() + JWT_EXPIRE_TIMEOUT))
                .issuer(JWT_ISSUER)
                .build();
    }
}
