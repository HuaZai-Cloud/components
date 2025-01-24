package cloud.huazai.objectstorage.object;

import io.github.artislong.utils.OssPathUtil;
import lombok.Data;

/**
 * ObjectStorageConfig
 *
 * @author devon
 * @since 2025/1/23
 */

@Data
public class ObjectStorageConfig {

    private String bucketName;

    private String endpoint;

    private String accessKey;

    private String accessSecret;

}
