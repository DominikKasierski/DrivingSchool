package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatisticsDto {

    private long lecturesHours;
    private long carriedOutPracticalHours ;
    private long scheduledPracticalHours;
    private String favouriteInstructor;
    private String favouriteCar;
}
