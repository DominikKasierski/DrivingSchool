package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AccessDto implements EntityToSign {

    private Long id;
    private AccessType accessType;
    private boolean activated;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d", version);
    }
}
