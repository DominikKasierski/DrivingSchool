package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRowDto {

    private Long id;
    private Long creationDate;
    private String firstname;
    private String lastname;
    private BigDecimal value;
}
