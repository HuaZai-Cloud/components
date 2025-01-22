package cloud.huazai.objectstorage.object;

import cloud.huazai.objectstorage.config.ObjectStorageProperties;
import cloud.huazai.objectstorage.config.Platform;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * ObjectStorageUpload
 *
 * @author devon
 * @since 2025/1/21
 */

public interface ObjectStorageUpload {


    String basicsUpload(String bucketName, File file);
    String basicsUpload(String bucketName, MultipartFile file);

    String basicsUpload(String bucketName, InputStream inputStream, String name);






}
