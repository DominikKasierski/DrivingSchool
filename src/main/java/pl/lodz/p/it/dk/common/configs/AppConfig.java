package pl.lodz.p.it.dk.common.configs;

import pl.lodz.p.it.dk.exceptions.AppRuntimeException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

@ApplicationScoped
public class AppConfig implements Serializable {

    private final Properties properties = new Properties();
    private static final String CONFIG_FILE = "app.config.properties";

    private static final String JWT_SECRET_KEY = "jwt.secretKey";
    private static final String JWT_EXPIRE_TIMEOUT = "jwt.expireTimeout";
    private static final String JWT_ISS = "jwt.issuer";

    @PostConstruct
    private void init() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw AppRuntimeException.configLoadException(e);
        }
    }

    private String get(String key) {
        return properties.getProperty(key);
    }

    private long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public String getJwtSecretKey() {
        return get(JWT_SECRET_KEY);
    }

    public Long getJwtExpireTimeout() {
        return getLong(JWT_EXPIRE_TIMEOUT);
    }

    public String getJwtIssuer() {
        return get(JWT_ISS);
    }


}
