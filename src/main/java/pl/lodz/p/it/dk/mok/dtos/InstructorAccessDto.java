package pl.lodz.p.it.dk.mok.dtos;

import lombok.*;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorAccessDto implements EntityToSign {

    private String login;
    private String firstname;
    private String lastname;
    private String permissions;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%s;%d", login, version);
    }
}
