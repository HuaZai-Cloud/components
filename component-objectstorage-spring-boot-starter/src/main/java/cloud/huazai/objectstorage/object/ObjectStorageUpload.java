package cloud.huazai.objectstorage.object;

import cloud.huazai.objectstorage.config.ObjectStorageProperties;
import cloud.huazai.objectstorage.config.Platform;

import java.io.File;

/**
 * ObjectStorageUpload
 *
 * @author devon
 * @since 2025/1/21
 */

public interface ObjectStorageUpload {


    String basicsUpload(Platform platform, String bucketName, File file);




}
