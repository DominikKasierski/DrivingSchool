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
@Size(min = 1, max = 128, message = "validation.confirmation.code.size")
@Pattern(regexp = RegularExpression.CONFIRMATION_CODE, message = "validation.confirmation.code.pattern")
public @interface Code {
    String value() default "validation.confirmation.code";
}
