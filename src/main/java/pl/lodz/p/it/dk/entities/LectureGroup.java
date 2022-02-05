package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecture_group")
@NoArgsConstructor
public class LectureGroup extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_lecture_group_id")
    @SequenceGenerator(name = "sequence_lecture_group_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "course_category", nullable = false, updatable = false)
    private CourseCategory courseCategory;

    @Getter
    @Setter
    @NotNull
    @OneToMany(mappedBy = "lecture_group", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> courses = new HashSet<>();

    @Getter
    @Setter
    @NotNull
    @OneToMany(mappedBy = "lecture_group", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Lecture> lectures = new HashSet<>();

    public LectureGroup(CourseCategory courseCategory, Set<Course> courses) {
        this.courseCategory = courseCategory;
        this.courses = courses;
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
