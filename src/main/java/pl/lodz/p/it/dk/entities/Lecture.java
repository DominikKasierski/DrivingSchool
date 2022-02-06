package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lecture")
@NoArgsConstructor
public class Lecture extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_lecture_id")
    @SequenceGenerator(name = "sequence_lecture_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @JoinColumn(name = "instructor_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private InstructorAccess instructor;

    @Getter
    @Setter
    @JoinColumn(name = "lecture_group_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private LectureGroup lectureGroup;

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

    public Lecture(InstructorAccess instructor, LectureGroup lectureGroup, Date dateFrom, Date dateTo) {
        this.instructor = instructor;
        this.lectureGroup = lectureGroup;
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
