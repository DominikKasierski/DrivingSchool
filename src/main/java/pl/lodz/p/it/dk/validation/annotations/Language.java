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
@Size(min = 2, max = 2, message = "validation.language.size")
@Pattern(regexp = RegularExpression.LANGUAGE, message = "validation.language.pattern")
public @interface Language {
    String message() default "validation.language";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
