package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainee_access")
@DiscriminatorValue("TRAINEE")
@NamedQueries({
        @NamedQuery(name = "TraineeAccess.findAll", query = "SELECT m FROM TraineeAccess m"),
        @NamedQuery(name = "TraineeAccess.findById", query = "SELECT m FROM TraineeAccess m WHERE m.id = :id")})
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class TraineeAccess extends Access implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @OneToMany(mappedBy = "trainee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> courses = new HashSet<>();
}
