package cloud.huazai.objectstorage.core;

import java.io.InputStream;
import java.util.Date;

/**
 * ObjectStorageClient
 *
 * @author devon
 * @since 2025/2/6
 */

public interface ObjectStorageClient {

    String uploadFile(String filePath,String fileName, InputStream inputStream,Date expiration);

    String multipartUploadFile(String filePath,String fileName,long fileSize,long partSize, InputStream inputStream,Date expiration);

    void deleteFile(String filePath, String fileName);

    String getSignedUrl(String filePath,String fileName,  Date expiration);

    void shutdownClient();
}
