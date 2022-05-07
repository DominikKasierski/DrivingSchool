package pl.lodz.p.it.dk.common.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {

    ACTIVATION_EMAIL("activation"),
    SUCCESSFUL_ACTIVATION_EMAIL("successful_activation"),
    DELETE_ACCOUNT_EMAIL("account_delete"),
    LOCK_EMAIL("lock"),
    UNLOCK_EMAIL("unlock"),
    EMAIL_CHANGE_EMAIL("email_change"),
    PASSWORD_RESET_EMAIL("password_reset"),
    ACCESS_GRANT_EMAIL("access_grant"),
    ACCESS_REVOKE_EMAIL("access_revoke"),
    PAYMENT_CONFIRM_EMAIL("payment_confirm"),
    COURSE_PAID_EMAIL("course_paid"),
    PAYMENT_REJECT_EMAIL("payment_reject"),
    PAYMENT_ADD_EMAIL("payment_add");

    private final String value;
}
