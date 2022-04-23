package pl.lodz.p.it.dk.validation.annotations;

import pl.lodz.p.it.dk.validation.RegularExpression;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Size(min = 5, max = 31, message = "validation.image.size")
@Pattern(regexp = RegularExpression.IMAGE, message = "validation.image.pattern")
public @interface Image {
    String message() default "validation.image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
