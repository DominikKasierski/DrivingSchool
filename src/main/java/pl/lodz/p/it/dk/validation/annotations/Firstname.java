package pl.lodz.p.it.dk.validation.annotations;

import pl.lodz.p.it.dk.validation.RegularExpression;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Size(min = 3, max = 31, message = "validation.firstname.size")
@Pattern(regexp = RegularExpression.FIRSTNAME, message = "validation.firstname.pattern")
public @interface Firstname {
    String value() default "validation.firstname";
}
