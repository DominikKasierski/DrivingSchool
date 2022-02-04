package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

import static pl.lodz.p.it.dk.entities.Course.TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT;

@Entity
@Table(name = "course", uniqueConstraints = {
        @UniqueConstraint(name = TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT, columnNames = {"trainee_id", "course_details_id"})
})
@NoArgsConstructor
@Getter
public class Course extends AbstractEntity implements Serializable {

    public static final String TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT = "constraint_course_trainee_id_course_details_id";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_course_id")
    @SequenceGenerator(name = "sequence_course_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @JoinColumn(name = "trainee_id",updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private TraineeAccess trainee;

    @Setter
    @JoinColumn(name = "course_details_id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private CourseDetails courseDetails;

    @Setter
    @JoinColumn(name = "lecture_group_id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private LectureGroup lectureGroup;

    @Setter
    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Payment> payments;

    @Setter
    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<DrivingLesson> drivingLessons;

    @Setter
    @NotNull
    @Column(name = "paid", nullable = false)
    private boolean paid = false;

    @Setter
    @NotNull
    @Column(name = "lectures_completion", nullable = false)
    private boolean lecturesCompletion = false;

    @Setter
    @NotNull
    @Column(name = "course_completion", nullable = false)
    private boolean courseCompletion = false;
}
