package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.validation.annotations.Image;
import pl.lodz.p.it.dk.validation.annotations.RegistrationNumber;
import pl.lodz.p.it.dk.validation.annotations.ValueOfEnum;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCarDto {

    @NotNull
    @ValueOfEnum(enumClass = CourseCategory.class)
    private String courseCategory;

    @NotNull
    @Image
    private String image;

    @NotNull
    @Size(min = 1, max = 31)
    private String brand;

    @NotNull
    @Size(min = 1, max = 31)
    private String model;

    @NotNull
    @RegistrationNumber
    private String registrationNumber;

    @NotNull
    @Min(value = 2010)
    @Max(value = 2022)
    @Digits(integer = 4, fraction = 0)
    private Integer productionYear;
}
