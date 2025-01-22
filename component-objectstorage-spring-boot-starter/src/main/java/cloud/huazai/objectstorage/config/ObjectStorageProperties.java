package cloud.huazai.objectstorage.config;

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


    private Map<String, StorageConfig> platformMap;

    public void setPlatformMap(Map<String, StorageConfig> platforms) {
        this.platformMap = platforms;
    }

    @Getter
    @Setter
    public static class StorageConfig {

        @NotBlank(message = "Access key is required.")
        private String accessKey;

        @NotBlank(message = "Secret key is required.")
        private String secretKey;

        @NotBlank(message = "Endpoint is required.")
        private String endpoint;

        @NotBlank(message = "Region is required.")
        private String region;

        @NotBlank(message = "BucketName is required.")
        private String bucketName;
    }
}
