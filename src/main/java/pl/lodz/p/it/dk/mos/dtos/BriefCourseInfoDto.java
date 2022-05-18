package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefCourseInfoDto {

    private String courseCategory;
    private BigDecimal price;
    private BigDecimal paid;
}
