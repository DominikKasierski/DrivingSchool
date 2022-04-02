package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Firstname;
import pl.lodz.p.it.dk.validation.annotations.Lastname;
import pl.lodz.p.it.dk.validation.annotations.Login;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {

    @Login
    private String login;

    @Firstname
    private String firstname;

    @Lastname
    private String lastname;
}
