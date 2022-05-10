package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.dk.entities.LectureGroup.NAME_CONSTRAINT;

@Entity
@Table(name = "lecture_group", uniqueConstraints = {
        @UniqueConstraint(name = NAME_CONSTRAINT, columnNames = {"name"})
})
@NamedQueries({
        @NamedQuery(name = "LectureGroup.findByName", query = "SELECT l FROM LectureGroup l WHERE l.name = :name"),
        @NamedQuery(name = "LectureGroup.findByCategory",
                query = "SELECT l FROM LectureGroup l WHERE l.courseCategory = :category")
})
@NoArgsConstructor
public class LectureGroup extends AbstractEntity implements Serializable {

    public static final String NAME_CONSTRAINT = "constraint_lecture_group_name";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_lecture_group_id")
    @SequenceGenerator(name = "sequence_lecture_group_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "course_category", nullable = false, updatable = false)
    private CourseCategory courseCategory;

    @Getter
    @Setter
    @OneToMany(mappedBy = "lectureGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> courses = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "lectureGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Lecture> lectures = new HashSet<>();

    public LectureGroup(String name, CourseCategory courseCategory) {
        this.name = name;
        this.courseCategory = courseCategory;
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
