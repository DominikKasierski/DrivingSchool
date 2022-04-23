package pl.lodz.p.it.dk.validation.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Size(min = 6, max = 127, message = "validation.email.address.size")
@Email(message = "validation.email.pattern")
public @interface EmailAddress {
    String message() default "validation.email.address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
