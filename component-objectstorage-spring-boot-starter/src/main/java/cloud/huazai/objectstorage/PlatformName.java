package cloud.huazai.objectstorage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PlatformName
 *
 * @author devon
 * @since 2025/1/21
 */


@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlatformNameValidator.class)
public @interface PlatformName {

    String message() default "Invalid platform name. Allowed platforms are: oss, cos, tos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
