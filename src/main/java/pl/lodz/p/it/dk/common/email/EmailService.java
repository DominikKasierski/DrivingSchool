package pl.lodz.p.it.dk.common.email;

import pl.lodz.p.it.dk.common.configs.AppConfig;
import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.exceptions.EmailException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class EmailService {

    @Inject
    private AppConfig appConfig;

    @Inject
    private I18n i18n;

    private static final String EMAIL_SUBJECT_FORMAT = "email.%s.subject";
    private static final String EMAIL_CONTENT_FORMAT = "email.%s.content";

    public void sendActivationEmail(Account account, String activationCode) throws BaseException {
        String language = account.getLanguage();
        String url = prepareUrl(appConfig.getActivationEndpoint(), activationCode);
        String subject = getEmailSubject(language, EmailType.ACTIVATION_EMAIL);
        String content = getEmailContent(language, EmailType.ACTIVATION_EMAIL, account.getLogin(), url);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendSuccessfulActivationEmail(Account account) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.SUCCESSFUL_ACTIVATION_EMAIL);
        String content = getEmailContent(language, EmailType.SUCCESSFUL_ACTIVATION_EMAIL, account.getLogin());
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendAccountDeletingEmail(Account account) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.DELETE_ACCOUNT_EMAIL);
        String content = getEmailContent(language, EmailType.DELETE_ACCOUNT_EMAIL, account.getLogin());
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendAccountLockingEmail(Account account) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.LOCK_EMAIL);
        String content = getEmailContent(language, EmailType.LOCK_EMAIL, account.getLogin());
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendAccountUnlockingEmail(Account account) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.UNLOCK_EMAIL);
        String content = getEmailContent(language, EmailType.UNLOCK_EMAIL, account.getLogin());
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendEmailChangingEmail(Account account, String emailChangeCode) throws BaseException {
        String language = account.getLanguage();
        String url = prepareUrl(appConfig.getEmailChangeEndpoint(), emailChangeCode);
        String subject = getEmailSubject(language, EmailType.EMAIL_CHANGE_EMAIL);
        String content = getEmailContent(language, EmailType.EMAIL_CHANGE_EMAIL, account.getLogin(), url);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendPasswordResetEmail(Account account, String passwordResetCode) throws BaseException {
        String language = account.getLanguage();
        String url = prepareUrl(appConfig.getPasswordResetEndpoint(), passwordResetCode);
        String subject = getEmailSubject(language, EmailType.PASSWORD_RESET_EMAIL);
        String content = getEmailContent(language, EmailType.PASSWORD_RESET_EMAIL, account.getLogin(), url);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendAccessGrantingEmail(Account account, String accessType) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.ACCESS_GRANT_EMAIL);
        String content = getEmailContent(language, EmailType.ACCESS_GRANT_EMAIL, account.getLogin(), accessType);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendAccessRevokingEmail(Account account, String accessType) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.ACCESS_REVOKE_EMAIL);
        String content = getEmailContent(language, EmailType.ACCESS_REVOKE_EMAIL, account.getLogin(), accessType);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendPaymentConfirmationEmail(Account account, String value) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.PAYMENT_CONFIRM_EMAIL);
        String content = getEmailContent(language, EmailType.PAYMENT_CONFIRM_EMAIL, account.getLogin(), value);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendCoursePaidEmail(Account account, String value) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.COURSE_PAID_EMAIL);
        String content = getEmailContent(language, EmailType.COURSE_PAID_EMAIL, account.getLogin(), value);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendPaymentRejectionEmail(Account account, String value) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.PAYMENT_REJECT_EMAIL);
        String content = getEmailContent(language, EmailType.PAYMENT_REJECT_EMAIL, account.getLogin(), value);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendPaymentAddingEmail(Account account, String value) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.PAYMENT_ADD_EMAIL);
        String content = getEmailContent(language, EmailType.PAYMENT_CONFIRM_EMAIL, account.getLogin(), value);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    public void sendDrivingLessonCancellationEmail(Account account, String value) throws BaseException {
        String language = account.getLanguage();
        String subject = getEmailSubject(language, EmailType.LESSON_CANCEL_EMAIL);
        String content = getEmailContent(language, EmailType.LESSON_CANCEL_EMAIL, account.getLogin(), value);
        sendEmail(account.getEmailAddress(), subject, content);
    }

    private String getEmailSubject(String language, EmailType emailType) {
        String mailType = String.format(EMAIL_SUBJECT_FORMAT, emailType.getValue());
        return i18n.getMessage(new Locale(language), mailType);
    }

    private String getEmailContent(String language, EmailType emailType, String... params) {
        String mailType = String.format(EMAIL_CONTENT_FORMAT, emailType.getValue());
        String pattern = i18n.getMessage(new Locale(language), mailType);
        return MessageFormat.format(pattern, (Object[]) params);
    }

    private String prepareUrl(String endpoint, String activationCode) {
        return String.join("/", appConfig.getEmailUri(), endpoint, activationCode);
    }

    private void sendEmail(String userEmail, String subject, String content) throws EmailException {
        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(appConfig.getEmailSender()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject(subject);
            message.setText(content, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw EmailException.emailException(e);
        }
    }

    private Session prepareSession() {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.host", appConfig.getEmailHost());
        sessionProperties.put("mail.smtp.port", appConfig.getEmailPort());
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(appConfig.getEmailUsername(), appConfig.getEmailPassword());
                    }
                });
    }
}
