package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.validation.annotations.Comment;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPaymentDto {

    @NotNull
    @Min(value = 0)
    @Digits(integer = 4, fraction = 0)
    private BigDecimal value;

    @Comment
    private String comment;

    public String getComment() {
        if (comment != null) {
            return comment;
        }
        return "";
    }
}
