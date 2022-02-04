package pl.lodz.p.it.dk.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_access")
@DiscriminatorValue("ADMIN")
@NamedQueries({
        @NamedQuery(name = "AdminAccess.findAll", query = "SELECT m FROM AdminAccess m"),
        @NamedQuery(name = "AdminAccess.findById", query = "SELECT m FROM AdminAccess m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class AdminAccess extends Access implements Serializable {

    private static final long serialVersionUID = 1L;
}
