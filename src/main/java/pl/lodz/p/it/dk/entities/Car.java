package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "car")
@NoArgsConstructor
public class Car extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_car_id")
    @SequenceGenerator(name = "sequence_car_id", allocationSize = 1)
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
    @Size(min = 1, max = 31)
    @Column(name = "image")
    private String image;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "brand", nullable = false)
    private String brand;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "model", nullable = false)
    private String model;

    @Getter
    @Setter
    @NotNull
    @Min(value = 2005)
    @Max(value = 2022)
    @Digits(integer = 4, fraction = 0)
    @Column(name = "production_year", nullable = false)
    private Integer productionYear;

    @Getter
    @Setter
    @OneToMany(mappedBy = "car", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<DrivingLesson> drivingLessons;

    public Car(CourseCategory courseCategory, String brand, String model, Integer productionYear) {
        this.courseCategory = courseCategory;
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
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
