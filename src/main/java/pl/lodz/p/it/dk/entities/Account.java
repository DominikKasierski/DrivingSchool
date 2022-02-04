package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

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
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByEmailAddress", query = "SELECT a FROM Account a WHERE a.emailAddress = :emailAddress"),
        @NamedQuery(name = "Account.findByNewEmailAddress", query = "SELECT a FROM Account a WHERE a.newEmailAddress = :newEmailAddress"),
        @NamedQuery(name = "Account.findByEnabled", query = "SELECT a FROM Account a WHERE a.blocked = :blocked"),
        @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed"),
        @NamedQuery(name = "Account.findByPhoneNumber",
                query = "SELECT a FROM Account a WHERE a.phoneNumber = :phoneNumber"),
        @NamedQuery(name = "Account.findUnverified",
                query = "SELECT a FROM Account a WHERE a.confirmed = false AND a.creationDate < :date"),
        @NamedQuery(name = "Account.findUnverifiedBetweenDate",
                query = "SELECT a FROM Account a WHERE a.confirmed = false AND a.creationDate BETWEEN :startDate AND" +
                        " :endDate AND a NOT IN (SELECT c.account FROM ConfirmationCode c WHERE used = false AND codeType = 0)")
})
@NoArgsConstructor
@Getter
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

    @Setter
    @NotNull
    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Setter
    @NotNull
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Setter
    @NotNull
    @Column(name = "login", updatable = false, nullable = false)
    private String login;

    @Setter
    @NotNull
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Setter
    @Column(name = "new_email_address")
    private String newEmailAddress = null;

    @Setter
    @NotNull
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Setter
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Setter
    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    @Setter
    @Min(value = 0)
    @Column(name = "failed_login_attempts", columnDefinition = "integer default 0")
    private Integer failedLoginAttempts = 0;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lock_modification_date")
    private Date lockModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "lock_modification_by")
    private Account lockModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirm_modification_date")
    private Date confirmModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "confirm_modification_by")
    private Account confirmModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "email_modification_date")
    private Date emailModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "email_modification_by")
    private Account emailModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "password_modification_date")
    private Date passwordModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "password_modification_by")
    private Account passwordModificationBy;

    @Setter
    @OneToMany(mappedBy = "account", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Access> accesses = new HashSet<>();

    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            mappedBy = "account", orphanRemoval = true)
    private Set<ConfirmationCode> confirmationCodes = new HashSet<>();
}

