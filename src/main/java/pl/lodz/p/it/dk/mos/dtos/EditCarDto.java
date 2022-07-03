package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Image;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditCarDto {

    @NotNull
    private Long id;

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
    @Min(value = 2010)
    @Max(value = 2022)
    @Digits(integer = 4, fraction = 0)
    private Integer productionYear;
}
