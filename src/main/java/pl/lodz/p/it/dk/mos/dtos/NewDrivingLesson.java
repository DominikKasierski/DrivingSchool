package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewDrivingLesson {

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    private Integer numberOfHours;

    @NotNull
    private Long dateFrom;

    @NotNull
    @Login
    private String instructorLogin;
}
