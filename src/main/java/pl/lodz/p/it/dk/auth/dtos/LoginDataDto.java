package pl.lodz.p.it.dk.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.dk.validation.annotations.Login;
import pl.lodz.p.it.dk.validation.annotations.Password;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDataDto {

    @NotNull
    @Login
    private String login;

    @NotNull
    @Password
    @ToString.Exclude
    private String password;
}
