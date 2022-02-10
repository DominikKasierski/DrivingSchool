package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.entities.enums.CodeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static pl.lodz.p.it.dk.entities.ConfirmationCode.CODE_CONSTRAINT;

@Entity
@Table(name = "confirmation_code", uniqueConstraints = {
        @UniqueConstraint(name = CODE_CONSTRAINT, columnNames = {"code"})
})
@NamedQueries({
        @NamedQuery(name = "ConfirmationCode.findByCode", query = "SELECT c FROM ConfirmationCode c WHERE c.code = :code")
})
@NoArgsConstructor
public class ConfirmationCode extends AbstractEntity implements Serializable {

    public static final String CODE_CONSTRAINT = "constraint_confirmation_code_code";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_confirmation_code_id")
    @SequenceGenerator(name = "sequence_confirmation_code_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Getter
    @Setter
    @NotNull
    @Column(name = "used", nullable = false)
    private boolean used;

    @Getter
    @Setter
    @NotNull
    @Column(name = "send_attempt", nullable = false)
    private int sendAttempt = 0;

    @Getter
    @Setter
    @JoinColumn(name = "account_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private Account account;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "code_type", nullable = false)
    private CodeType codeType;

    public ConfirmationCode(String code, boolean used, Account account, CodeType codeType, Account createdBy) {
        this.code = code;
        this.used = used;
        this.account = account;
        this.codeType = codeType;
        this.setCreatedBy(createdBy);
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
