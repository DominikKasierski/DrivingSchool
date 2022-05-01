package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private String paymentStatus;
    private Long courseId;
    private BigDecimal value;
    private String traineeComment;
    private String adminComment;
    private Date creationDate;
    private Date modificationDate;
}
