package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Comment;
import pl.lodz.p.it.dk.validation.annotations.Login;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectPaymentDto {

    @Login
    private String login;

    @Comment
    private String adminComment;

    public String getAdminComment() {
        if (adminComment != null) {
            return adminComment;
        }
        return "";
    }
}
