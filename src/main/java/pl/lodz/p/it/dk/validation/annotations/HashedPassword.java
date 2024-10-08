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
@Size(min = 64, max = 64, message = "validation.hashed.password.size")
@Pattern(regexp = RegularExpression.HASHED_PASSWORD, message = "validation.hashed.password.pattern")
public @interface HashedPassword {
    String message() default "validation.hashed.password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
