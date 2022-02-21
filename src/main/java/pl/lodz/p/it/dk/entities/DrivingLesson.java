package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.entities.enums.LessonStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "driving_lesson")
@NamedQueries({
        @NamedQuery(name = "DrivingLesson.findAllActive",
                query = "SELECT d FROM DrivingLesson d WHERE d.lessonStatus = pl.lodz.p.it.dk.entities.enums" +
                        ".LessonStatus.PENDING OR d.lessonStatus = pl.lodz.p.it.dk.entities.enums.LessonStatus" +
                        ".IN_PROGRESS"),
        @NamedQuery(name = "DrivingLesson.findAllArchive",
                query = "SELECT d FROM DrivingLesson d WHERE d.lessonStatus = pl.lodz.p.it.dk.entities.enums" +
                        ".LessonStatus.CANCELLED OR d.lessonStatus = pl.lodz.p.it.dk.entities.enums.LessonStatus" +
                        ".FINISHED")
})
@NoArgsConstructor
public class DrivingLesson extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_driving_lesson_id")
    @SequenceGenerator(name = "sequence_driving_lesson_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_status", nullable = false)
    private LessonStatus lessonStatus = LessonStatus.PENDING;

    @Getter
    @Setter
    @JoinColumn(name = "instructor_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private InstructorAccess instructor;

    @Getter
    @Setter
    @JoinColumn(name = "course_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private Course course;

    @Getter
    @Setter
    @JoinColumn(name = "car_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private Car car;

    @Getter
    @Setter
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_from", nullable = false)
    private Date dateFrom;

    @Getter
    @Setter
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_to", nullable = false)
    private Date dateTo;

    public DrivingLesson(InstructorAccess instructor, Course course, Car car, Date dateFrom, Date dateTo) {
        this.instructor = instructor;
        this.course = course;
        this.car = car;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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
