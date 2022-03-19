package pl.lodz.p.it.dk.security;

import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@RequestScoped
public class AuthenticationMechanism implements HttpAuthenticationMechanism {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Inject
    JwtUtils jwtUtils;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) {

        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            if (httpMessageContext.isProtected()) {
                return httpMessageContext.responseUnauthorized();
            }
            return httpMessageContext.doNothing();
        }

        String token = authorizationHeader.substring(BEARER.length());

        if (jwtUtils.verify(token)) {
            return getAuthenticationStatus(httpMessageContext, token);
        }
        return httpMessageContext.responseUnauthorized();
    }

    private AuthenticationStatus getAuthenticationStatus(HttpMessageContext httpMessageContext, String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            String login = jwt.getJWTClaimsSet().getSubject();
            String roles = jwt.getJWTClaimsSet().getStringClaim("roles");
            Date expirationTime = (Date) (jwt.getJWTClaimsSet().getClaim("exp"));

            if (new Date().after(expirationTime)) {
                return httpMessageContext.responseUnauthorized();
            }
            return httpMessageContext
                    .notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(roles.split(","))));
        } catch (ParseException e) {
            return httpMessageContext.responseUnauthorized();
        }
    }
}
