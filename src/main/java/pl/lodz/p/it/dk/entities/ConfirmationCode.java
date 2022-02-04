package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.CodeType;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static pl.lodz.p.it.dk.entities.ConfirmationCode.CODE_CONSTRAINT;

@Entity
@Table(name = "confirmation_code", uniqueConstraints = {
        @UniqueConstraint(name = CODE_CONSTRAINT, columnNames = {"code"})
})
@NamedQueries({
        @NamedQuery(name = "ConfirmationCode.findAll", query = "SELECT p FROM ConfirmationCode p"),
        @NamedQuery(name = "ConfirmationCode.findById", query = "SELECT p FROM ConfirmationCode p WHERE p.id = :id"),
        @NamedQuery(name = "ConfirmationCode.findResetCodesByAccount",
                query = "SELECT p FROM ConfirmationCode p WHERE (p.account = :account AND p.codeType = 2 AND p.used =" +
                        " false) ORDER BY p.creationDate ASC"),
        @NamedQuery(name = "ConfirmationCode.findByCode",
                query = "SELECT p FROM ConfirmationCode p WHERE p.code = :code"),
        @NamedQuery(name = "ConfirmationCode.findNotUsedByAccount",
                query = "SELECT p FROM ConfirmationCode p WHERE p.account = :account AND p.used = false AND p" +
                        ".codeType = 0"),
        @NamedQuery(name = "ConfirmationCode.findAllByCodeType",
                query = "SELECT p FROM ConfirmationCode p WHERE p.account = :account AND p.codeType = :codeType AND p" +
                        ".used = :isUsed"),
        @NamedQuery(name = "ConfirmationCode.findAllAccountsWithUnusedCodes",
                query = "SELECT p.account FROM ConfirmationCode p WHERE p.codeType = :codeType AND p.creationDate < " +
                        ":date AND p.used = false"),
        @NamedQuery(name = "ConfirmationCode.findUnusedCodeByAccount",
                query = "SELECT p FROM ConfirmationCode p WHERE p.account = :account AND p.used = false AND p" +
                        ".codeType = :codeType"),
        @NamedQuery(name = "ConfirmationCode.findAllUnusedByCodeTypeAndBeforeAndAttemptCount",
                query = "SELECT p FROM ConfirmationCode p WHERE p.used = false AND p.codeType = :type AND p" +
                        ".creationDate < :date AND p.sendAttempt = :attempts")
}
)
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
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
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
