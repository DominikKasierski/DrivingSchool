package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

import static pl.lodz.p.it.dk.entities.Course.TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT;

@Entity
@Table(name = "course", uniqueConstraints = {
        @UniqueConstraint(name = TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT,
                columnNames = {"trainee_id", "course_details_id"})
})
@NamedQueries({
        @NamedQuery(name = "Course.findByCategory",
                query = "SELECT c FROM Course c WHERE c.courseDetails.courseCategory = :category"),
        @NamedQuery(name = "Course.findByTraineeId", query = "SELECT c FROM Course c WHERE c.trainee.id = :traineeId"),
        @NamedQuery(name = "Course.findByLectureGroupId",
                query = "SELECT c FROM Course c WHERE c.lectureGroup.id = :lectureGroupId")
})
@NoArgsConstructor
public class Course extends AbstractEntity implements Serializable {

    public static final String TRAINEE_ID_COURSE_DETAILS_ID_CONSTRAINT =
            "constraint_course_trainee_id_course_details_id";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_course_id")
    @SequenceGenerator(name = "sequence_course_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @JoinColumn(name = "trainee_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private TraineeAccess trainee;

    @Getter
    @Setter
    @JoinColumn(name = "course_details_id")
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private CourseDetails courseDetails;

    @Getter
    @Setter
    @JoinColumn(name = "lecture_group_id")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LectureGroup lectureGroup;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Payment> payments;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<DrivingLesson> drivingLessons;

    @Getter
    @Setter
    @NotNull
    @Column(name = "advance", nullable = false)
    private boolean advance = false;

    @Getter
    @Setter
    @NotNull
    @Column(name = "paid", nullable = false)
    private boolean paid = false;

    @Getter
    @Setter
    @NotNull
    @Column(name = "lectures_completion", nullable = false)
    private boolean lecturesCompletion = false;

    @Getter
    @Setter
    @NotNull
    @Column(name = "driving_completion", nullable = false)
    private boolean drivingCompletion = false;

    @Getter
    @Setter
    @NotNull
    @Column(name = "course_completion", nullable = false)
    private boolean courseCompletion = false;

    public Course(TraineeAccess trainee, CourseDetails courseDetails) {
        this.trainee = trainee;
        this.courseDetails = courseDetails;
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
