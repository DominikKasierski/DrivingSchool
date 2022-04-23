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
@Size(min = 1, max = 128, message = "validation.confirmation.code.size")
@Pattern(regexp = RegularExpression.CONFIRMATION_CODE, message = "validation.confirmation.code.pattern")
public @interface Code {
    String message() default "validation.confirmation.code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
