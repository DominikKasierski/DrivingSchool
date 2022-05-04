package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentForApprovalDto {

    private Date creationDate;
    private String firstname;
    private String lastname;
    private String courseCategory;
    private BigDecimal value;
    private String traineeComment;
}
