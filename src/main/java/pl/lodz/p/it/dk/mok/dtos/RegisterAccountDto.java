package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.dk.validation.annotations.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountDto {

    @NotNull
    @Login
    private String login;

    @NotNull
    @EmailAddress
    private String emailAddress;

    @NotNull
    @Password
    @ToString.Exclude
    private String password;

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
