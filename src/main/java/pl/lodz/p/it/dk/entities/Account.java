package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.validation.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.dk.entities.Account.*;

@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(name = LOGIN_CONSTRAINT, columnNames = {"login"}),
        @UniqueConstraint(name = EMAIL_ADDRESS_CONSTRAINT, columnNames = {"email_address"}),
        @UniqueConstraint(name = PHONE_NUMBER_CONSTRAINT, columnNames = {"phone_number"})
})
@NamedQueries({
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByEmailAddress", query = "SELECT a FROM Account a WHERE a.emailAddress = :emailAddress")
})
@NoArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    public static final String LOGIN_CONSTRAINT = "constraint_account_login";
    public static final String EMAIL_ADDRESS_CONSTRAINT = "constraint_account_email_address";
    public static final String PHONE_NUMBER_CONSTRAINT = "constraint_account_phone_number";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_account_id")
    @SequenceGenerator(name = "sequence_account_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Getter
    @Setter
    @NotNull
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Getter
    @Setter
    @NotNull
    @Login
    @Column(name = "login", updatable = false, nullable = false)
    private String login;

    @Getter
    @Setter
    @NotNull
    @EmailAddress
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Getter
    @Setter
    @EmailAddress
    @Column(name = "new_email_address")
    private String newEmailAddress = null;

    @Getter
    @Setter
    @NotNull
    @HashedPassword
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Getter
    @Setter
    @NotNull
    @Firstname
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Getter
    @Setter
    @NotNull
    @Lastname
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Getter
    @Setter
    @Language
    @Column(name = "language", nullable = false)
    private String language;

    @Getter
    @Setter
    @PhoneNumber
    @Column(name = "phone_number")
    private String phoneNumber;

    @Getter
    @Setter
    @Min(value = 0)
    @Column(name = "failed_login_attempts", columnDefinition = "integer default 0")
    private Integer failedLoginAttempts = 0;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lock_modification_date")
    private Date lockModificationDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "lock_modification_by")
    private Account lockModificationBy;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirm_modification_date")
    private Date confirmModificationDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "confirm_modification_by")
    private Account confirmModificationBy;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "email_modification_date")
    private Date emailModificationDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "email_modification_by")
    private Account emailModificationBy;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "password_modification_date")
    private Date passwordModificationDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "password_modification_by")
    private Account passwordModificationBy;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Access> accesses = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ConfirmationCode> confirmationCodes = new HashSet<>();

    public Account(String login, String password, String firstname, String lastname, boolean confirmed,
                   boolean blocked) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.confirmed = confirmed;
        this.blocked = blocked;
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

