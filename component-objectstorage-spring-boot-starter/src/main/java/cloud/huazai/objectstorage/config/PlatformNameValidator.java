package cloud.huazai.objectstorage.config;

import cloud.huazai.objectstorage.config.PlatformName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * PlatformNameValidator
 *
 * @author devon
 * @since 2025/1/21
 */

public class PlatformNameValidator implements ConstraintValidator<PlatformName, String> {

    private final List<String> allowedPlatforms = Arrays.asList(Platform.OSS.getPlatform(), Platform.COS.getPlatform(), Platform.TOS.getPlatform());

    @Override
    public void initialize(PlatformName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return allowedPlatforms.contains(value);
    }


}
