package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String name;
    private String type;
    private Date startTime;
    private Date endTime;
}
