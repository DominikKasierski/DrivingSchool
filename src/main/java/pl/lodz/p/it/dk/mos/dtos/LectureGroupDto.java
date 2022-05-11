package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureGroupDto implements EntityToSign {

    private Long id;
    private String name;
    private String courseCategory;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
