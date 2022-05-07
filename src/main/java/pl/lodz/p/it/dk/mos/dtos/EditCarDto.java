package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Image;
import pl.lodz.p.it.dk.validation.annotations.RegistrationNumber;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @RegistrationNumber
    private String registrationNumber;
}
