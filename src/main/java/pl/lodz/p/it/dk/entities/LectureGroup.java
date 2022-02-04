package pl.lodz.p.it.dk.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
public class LectureGroup extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_lecture_group_id")
    @SequenceGenerator(name = "sequence_lecture_group_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "course_category", nullable = false)
    private CourseCategory courseCategory;

    @Setter
    @NotNull
    @OneToMany(mappedBy = "lecture_group", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Course> courses = new HashSet<>();

    @Setter
    @NotNull
    @OneToMany(mappedBy = "lecture_group", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Lecture> lectures = new HashSet<>();
}
