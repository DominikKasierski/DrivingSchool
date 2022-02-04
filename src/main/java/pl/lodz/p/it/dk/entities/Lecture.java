package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lecture")
@NoArgsConstructor
@Getter
public class Lecture extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_lecture_id")
    @SequenceGenerator(name = "sequence_lecture_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @JoinColumn(name = "instructor_id", nullable = false)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private InstructorAccess instructor;

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
    @JoinColumn(name = "lecture_group_id", updatable = false)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private LectureGroup lectureGroup;
}
