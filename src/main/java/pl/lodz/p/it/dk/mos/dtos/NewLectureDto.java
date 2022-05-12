package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Login;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewLectureDto {

    @NotNull
    @Login
    private String instructorLogin;

    @NotNull
    private Long lectureGroupId;

    @NotNull
    private Date dateFrom;

    @NotNull
    private Date dateTo;
}
