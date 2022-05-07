package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto implements EntityToSign {

    private Long id;
    private String courseCategory;
    private String image;
    private String brand;
    private String model;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
