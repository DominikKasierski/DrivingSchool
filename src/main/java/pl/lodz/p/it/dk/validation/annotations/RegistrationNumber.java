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
@Size(min = 4, max = 7, message = "validation.registration.number.size")
@Pattern(regexp = RegularExpression.REGISTRATION_NUMBER, message = "validation.registration.number.pattern")
public @interface RegistrationNumber {
    String message() default "validation.phone.number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
