package pl.lodz.p.it.dk.common.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {

    ACTIVATION_MAIL("activation"),
    SUCCESSFUL_ACTIVATION_MAIL("successful_activation");

    private final String value;
}
