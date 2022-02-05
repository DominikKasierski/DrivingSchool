package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.dk.entities.CourseDetails.COURSE_CATEGORY_CONSTRAINT;

@Entity
@Table(name = "course_details", uniqueConstraints = {
        @UniqueConstraint(name = COURSE_CATEGORY_CONSTRAINT, columnNames = {"course_category"})
})
@NoArgsConstructor
public class CourseDetails extends AbstractEntity implements Serializable {

    public static final String COURSE_CATEGORY_CONSTRAINT = "constraint_course_details_course_category";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_course_details_id")
    @SequenceGenerator(name = "sequence_course_details_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "course_category", nullable = false)
    private CourseCategory courseCategory;

    @Getter
    @Setter
    @NotNull
    @Min(value = 1000)
    @Digits(integer = 4, fraction = 0)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Getter
    @Setter
    @NotNull
    @Min(value = 5)
    @Digits(integer = 2, fraction = 0)
    @Column(name = "lectures_hours", nullable = false)
    private Integer lecturesHours;

    @Getter
    @Setter
    @NotNull
    @Min(value = 5)
    @Digits(integer = 2, fraction = 0)
    @Column(name = "driving_hours", nullable = false)
    private Integer drivingHours;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course_details", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> courses = new HashSet<>();

    public CourseDetails(CourseCategory courseCategory, BigDecimal price, Integer lecturesHours, Integer drivingHours) {
        this.courseCategory = courseCategory;
        this.price = price;
        this.lecturesHours = lecturesHours;
        this.drivingHours = drivingHours;
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



