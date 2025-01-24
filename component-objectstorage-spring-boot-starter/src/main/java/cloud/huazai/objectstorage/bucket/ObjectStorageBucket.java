package cloud.huazai.objectstorage.bucket;

import cloud.huazai.objectstorage.constant.PlatformType;

import java.util.List;

/**
 * CreateBucket
 *
 * @author devon
 * @since 2025/1/21
 */

public interface ObjectStorageBucket {

    void createBucket(PlatformType platform, String bucketName);

    List<String> getBucket(PlatformType platform);

    void deleteBucket(PlatformType platform, String bucketName);
}
