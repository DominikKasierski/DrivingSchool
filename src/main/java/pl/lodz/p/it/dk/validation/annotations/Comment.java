package pl.lodz.p.it.dk.validation.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Size(min = 4, max = 255, message = "validation.comment.size")
public @interface Comment {
    String message() default "validation.comment";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
