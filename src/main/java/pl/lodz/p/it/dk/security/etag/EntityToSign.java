package pl.lodz.p.it.dk.security.etag;

import javax.json.bind.annotation.JsonbTransient;

public interface EntityToSign {

    @JsonbTransient
    String getMessageToSign();
}
