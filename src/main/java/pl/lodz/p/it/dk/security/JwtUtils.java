package pl.lodz.p.it.dk.security;

import pl.lodz.p.it.dk.common.configs.AppConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
}
