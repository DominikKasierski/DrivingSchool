package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.dk.validation.annotations.Login;
import pl.lodz.p.it.dk.validation.annotations.Password;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOtherPasswordDto {

    @NotNull
    @Login
    private String login;

    @NotNull
    @Password
    @ToString.Exclude
    private String newPassword;
}
