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
@Size(min = 8, max = 64, message = "validation.password.size")
@Pattern(regexp = RegularExpression.PASSWORD, message = "validation.password.pattern")
public @interface Password {
    String message() default "validation.password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
