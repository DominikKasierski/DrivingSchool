package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.AccessType;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static pl.lodz.p.it.dk.entities.Access.ACCESS_TYPE_ACCOUNT_ID_CONSTRAINT;

@Entity
@Table(name = "access", uniqueConstraints = {
        @UniqueConstraint(name = ACCESS_TYPE_ACCOUNT_ID_CONSTRAINT, columnNames = {"access_type", "account_id"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_type")
@NoArgsConstructor
public abstract class Access extends AbstractEntity implements Serializable {

    public static final String ACCESS_TYPE_ACCOUNT_ID_CONSTRAINT = "constraint_access_access_type_account_id";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_access_id")
    @SequenceGenerator(name = "sequence_access_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", updatable = false, nullable = false)
    private AccessType accessType;

    @Getter
    @Setter
    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

    @Getter
    @Setter
    @JoinColumn(name = "account_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private Account account;

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
