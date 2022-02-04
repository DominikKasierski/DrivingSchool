package pl.lodz.p.it.dk.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.dk.entities.enums.PaymentStatus;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@Getter
public class Payment extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_payment_id")
    @SequenceGenerator(name = "sequence_payment_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @JoinColumn(name = "course_id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
    private Course course;

    @Setter
    @NotNull
    @Min(value = 0)
    @Digits(integer = 4, fraction = 2)
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Setter
    @Size(min = 4, max = 255)
    @Column(name = "comment")
    private String comment;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.IN_PROGRESS;
}
