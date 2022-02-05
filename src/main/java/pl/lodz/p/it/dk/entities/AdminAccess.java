package pl.lodz.p.it.dk.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_access")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
@ToString(callSuper = true)
public class AdminAccess extends Access implements Serializable {

    private static final long serialVersionUID = 1L;
}
