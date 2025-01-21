package cloud.huazai.objectstorage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * ObjectStorageProperties
 *
 * @author devon
 * @since 2025/1/21
 */
@Getter
@Component
@ConfigurationProperties(prefix = "huazai.objectstorage")
@Validated
public class ObjectStorageProperties {


    private Map<@PlatformName String, PlatformConfig> platforms;

    public void setPlatforms(Map<String, PlatformConfig> platforms) {
        this.platforms = platforms;
    }

    @Getter
    @Setter
    public static class PlatformConfig {

        @NotBlank(message = "Access key is required.")
        private String accessKey;

        @NotBlank(message = "Secret key is required.")
        private String secretKey;

        @NotBlank(message = "Endpoint is required.")
        private String endpoint;

        @NotBlank(message = "Region is required.")
        private String region;
    }
}
