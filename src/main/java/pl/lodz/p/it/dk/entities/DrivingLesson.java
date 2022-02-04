package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "driving_lesson")
@NoArgsConstructor
@Getter
public class DrivingLesson extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_driving_lesson_id")
    @SequenceGenerator(name = "sequence_driving_lesson_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @JoinColumn(name = "instructor_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private InstructorAccess instructor;

    @Setter
    @JoinColumn(name = "course_id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
    private Course course;

    @Setter
    @JoinColumn(name = "car_id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
    private Car car;

    @Setter
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_from", nullable = false)
    private Date dateFrom;

    @Setter
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_to", nullable = false)
    private Date dateTo;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_status", nullable = false)
    private LessonStatus lessonStatus = LessonStatus.PENDING;
}
