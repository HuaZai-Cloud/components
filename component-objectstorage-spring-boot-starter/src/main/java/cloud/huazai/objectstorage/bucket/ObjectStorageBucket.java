package cloud.huazai.objectstorage.bucket;

import cloud.huazai.objectstorage.config.Platform;

import java.util.List;

/**
 * CreateBucket
 *
 * @author devon
 * @since 2025/1/21
 */

public interface ObjectStorageBucket {

    void createBucket(Platform platform, String bucketName);

    List<String> getBucket(Platform platform);

    void deleteBucket(Platform platform, String bucketName);
}
