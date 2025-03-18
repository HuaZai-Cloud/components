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

    String uploadFile(String fileName,String filePath, InputStream inputStream,Date expiration);

    String getSignedUrl(String fileName, String filePath, Date expiration);
}
