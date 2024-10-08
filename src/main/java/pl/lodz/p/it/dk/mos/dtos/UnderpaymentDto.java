package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderpaymentDto {

    private String courseCategory;
    private String login;
    private String firstname;
    private String lastname;
    private BigDecimal price;
    private BigDecimal paid;
}
