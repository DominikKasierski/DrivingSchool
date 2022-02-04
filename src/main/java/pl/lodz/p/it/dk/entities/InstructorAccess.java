package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instructor_access")
@DiscriminatorValue("INSTRUCTOR")
@NamedQueries({
        @NamedQuery(name = "InstructorAccess.findAll", query = "SELECT m FROM InstructorAccess m"),
        @NamedQuery(name = "InstructorAccess.findById", query = "SELECT m FROM InstructorAccess m WHERE m.id = :id")})
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class InstructorAccess extends Access implements Serializable {

    private static final long serialVersionUID = 1L;

    //TODO: Sprawdzić czy działa!!

    @ElementCollection(targetClass = CourseCategory.class)
    @JoinTable(name = "instructors_permissions", joinColumns = @JoinColumn(name = "instructor_id"))
    @Column(name = "permissions")
    @Enumerated(EnumType.STRING)
    private Set<CourseCategory> permissions = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "instructor_access", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Lecture> lectures = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "instructor_access", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<DrivingLesson> drivingLessons = new HashSet<>();
}
