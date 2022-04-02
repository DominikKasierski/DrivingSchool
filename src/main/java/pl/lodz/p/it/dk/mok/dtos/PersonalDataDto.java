package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Firstname;
import pl.lodz.p.it.dk.validation.annotations.Lastname;
import pl.lodz.p.it.dk.validation.annotations.PhoneNumber;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDto {

    @NotNull
    @Firstname
    private String firstname;

    @NotNull
    @Lastname
    private String lastname;

    @NotNull
    @PhoneNumber
    private String phoneNumber;
}
