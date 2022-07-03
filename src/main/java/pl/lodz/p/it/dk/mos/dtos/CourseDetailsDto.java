package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailsDto {

    private Long id;
    private String courseCategory;
    private BigDecimal price;
    private Integer lecturesHours;
    private Integer drivingHours;
}
