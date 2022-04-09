package pl.lodz.p.it.dk.common.email;

import lombok.extern.java.Log;

import javax.enterprise.context.RequestScoped;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log
@RequestScoped
public class I18n {

    private static final String BUNDLE_BASE_NAME = "messages";

    public String getMessage(Locale locale, String key) {
        try {
            Locale currentLocale = Locale.getDefault();
            Locale.setDefault(locale);
            String message = getBundle(locale).getString(key);
            Locale.setDefault(currentLocale);
            return message;
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            log.warning("Error during: '" + key + "' internationalization. Exception: " + e.getMessage());
        }
        return key;
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }
}
