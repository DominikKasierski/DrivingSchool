package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.PaymentStatus;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.validation.annotations.Comment;

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
public class Payment extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_payment_id")
    @SequenceGenerator(name = "sequence_payment_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.IN_PROGRESS;

    @Getter
    @Setter
    @JoinColumn(name = "course_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private Course course;

    @Getter
    @Setter
    @NotNull
    @Min(value = 0)
    @Digits(integer = 4, fraction = 2)
    @Column(name = "value", nullable = false, updatable = false)
    private BigDecimal value;

    @Getter
    @Setter
    @Size(min = 4, max = 255)
    @Comment
    @Column(name = "comment")
    private String comment;

    public Payment(Course course, BigDecimal value) {
        this.course = course;
        this.value = value;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}
