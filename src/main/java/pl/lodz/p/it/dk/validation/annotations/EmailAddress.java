package pl.lodz.p.it.dk.validation.annotations;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Size(min = 6, max = 127, message = "validation.email.address.size")
@Email(message = "validation.email.pattern")
public @interface EmailAddress {
    String value() default "validation.email.address";
}
