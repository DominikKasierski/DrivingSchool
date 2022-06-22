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

    private static final String JWT_SECRET_KEY = "jwt.secret_key";
    private static final String JWT_EXPIRE_TIMEOUT = "jwt.expire_timeout";
    private static final String JWT_ISSUER = "jwt.issuer";

    private static final String ETAG_SECRET_KEY = "etag.secret_key";

    private static final String ACTIVATION_ENDPOINT = "email.endpoint.activation";
    private static final String EMAIL_CHANGE_ENDPOINT = "email.endpoint.email_change";
    private static final String PASSWORD_RESET_ENDPOINT = "email.endpoint.password_reset";

    private static final String EMAIL_URI = "email.property.uri";
    private static final String EMAIL_HOST = "email.property.host";
    private static final String EMAIL_PORT = "email.property.port";
    private static final String EMAIL_SENDER = "email.property.sender";
    private static final String EMAIL_USERNAME = "email.property.username";
    private static final String EMAIL_PASSWORD = "email.property.password";

    private static final String PASSWORD_RESET_SECONDS_LIMIT = "password.reset_milliseconds_limit";

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
        return get(JWT_ISSUER);
    }

    public String getEtagSecretKey() {
        return get(ETAG_SECRET_KEY);
    }

    public String getActivationEndpoint() {
        return get(ACTIVATION_ENDPOINT);
    }

    public String getEmailChangeEndpoint() {
        return get(EMAIL_CHANGE_ENDPOINT);
    }

    public String getPasswordResetEndpoint() {
        return get(PASSWORD_RESET_ENDPOINT);
    }

    public String getEmailUri() {
        return get(EMAIL_URI);
    }

    public String getEmailHost() {
        return get(EMAIL_HOST);
    }

    public String getEmailPort() {
        return get(EMAIL_PORT);
    }

    public String getEmailSender() {
        return get(EMAIL_SENDER);
    }

    public String getEmailUsername() {
        return get(EMAIL_USERNAME);
    }

    public String getEmailPassword() {
        return get(EMAIL_PASSWORD);
    }

    public Long getPasswordResetSecondsLimit() {
        return getLong(PASSWORD_RESET_SECONDS_LIMIT);
    }
}
