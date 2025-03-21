package cloud.huazai.objectstorage.properties;

import lombok.Data;

/**
 * ObjectStoragePlatformProperties
 *
 * @author devon
 * @since 2025/2/6
 */

@Data
public class ObjectStoragePlatformProperties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String endpoint;
    private String bucket;

}
