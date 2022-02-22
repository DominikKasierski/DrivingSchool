package pl.lodz.p.it.dk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.common.abstracts.AbstractEntity;
import pl.lodz.p.it.dk.validation.annotations.Image;
import pl.lodz.p.it.dk.validation.annotations.RegistrationNumber;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "car")
@NamedQueries({
        @NamedQuery(name = "Car.findByCategory", query = "SELECT c FROM Car c WHERE c.courseCategory = :category AND c.deleted = false")
})
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
    @Column(name = "course_category", updatable = false, nullable = false)
    private CourseCategory courseCategory;

    @Getter
    @Setter
    @Size(min = 1, max = 31)
    @Image
    @Column(name = "image")
    private String image;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "brand", updatable = false, nullable = false)
    private String brand;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "model", updatable = false, nullable = false)
    private String model;

    @Getter
    @Setter
    @NotNull
    @RegistrationNumber
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Getter
    @Setter
    @NotNull
    @Min(value = 2005)
    @Max(value = 2022)
    @Digits(integer = 4, fraction = 0)
    @Column(name = "production_year", updatable = false, nullable = false)
    private Integer productionYear;

    @Getter
    @Setter
    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<DrivingLesson> drivingLessons;

    @Getter
    @Setter
    @NotNull
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

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
