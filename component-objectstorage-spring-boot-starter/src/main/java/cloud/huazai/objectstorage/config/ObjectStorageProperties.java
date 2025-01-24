package cloud.huazai.objectstorage.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectStorageProperties
 *
 * @author devon
 * @since 2025/1/21
 */
@Data
// @ConfigurationProperties(prefix = "huazai.objectstorage")
public class ObjectStorageProperties {


    private Map<String, ProviderProperties> providers = new HashMap<>();

    @Data
    public static class ProviderProperties {
        private String accessKey;
        private String secretKey;
        private String endpoint;
        private String region;
        private String bucketName;
    }
}
