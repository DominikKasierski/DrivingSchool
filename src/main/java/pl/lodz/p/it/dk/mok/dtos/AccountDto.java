package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.security.etag.EntityToSign;
import pl.lodz.p.it.dk.validation.annotations.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements EntityToSign {

    @Login
    private String login;

    @EmailAddress
    private String emailAddress;

    @Firstname
    private String firstname;

    @Lastname
    private String lastname;

    @Language
    private String language;

    @PhoneNumber
    private String phoneNumber;

    private boolean enabled;

    private boolean confirmed;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%s;%d", login, version);
    }
}
