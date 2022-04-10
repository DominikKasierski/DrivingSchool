package pl.lodz.p.it.dk.common.abstracts;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

// TODO: Ogarnąć ToStringa

@MappedSuperclass
public abstract class AbstractEntity {

    @Getter
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date")
    private Date modificationDate;

    @Getter
    @Setter
    @NotNull
    @OneToOne
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private Account createdBy;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "modified_by")
    private Account modifiedBy;

    @Getter
    @Version
    private Long version;

    @PrePersist
    private void prePersist() {
        creationDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    private void preUpdate() {
        modificationDate = new Date(System.currentTimeMillis());
    }

    public abstract Long getId();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != this.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        return (this.getId() != null || other.getId() == null) &&
                (this.getId() == null || this.getId().equals(other.getId()));
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this)
                .append("creationDate", creationDate)
                .append("modificationDate", modificationDate)
                .append("version", version)
                .append("createdBy", createdBy == null ? "" : createdBy.getId());
        if (modifiedBy != null) {
            toStringBuilder.append("modifiedBy", modifiedBy.getId());
        }
        return toStringBuilder.toString();
    }
}
