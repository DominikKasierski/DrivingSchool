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
@Size(min = 2, max = 31, message = "validation.lastname.size")
@Pattern(regexp = RegularExpression.LASTNAME, message = "validation.lastname.pattern")
public @interface Lastname {
    String message() default "validation.lastname";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
