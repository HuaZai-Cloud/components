package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

/**
 * TencentCosClient
 *
 * @author devon
 * @since 2025/2/6
 */

public class TencentCosClient {

    // private final OSS ossClient;

    // private final COSClient cosClient;
    private final String bucket;

    public TencentCosClient(ObjectStoragePlatformProperties properties) {

        // this.ossClient = new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
        this.bucket = properties.getBucket();
    }


}
