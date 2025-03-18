package cloud.huazai.objectstorage.core;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;

/**
 * ObjectStorageService
 *
 * @author devon
 * @since 2025/2/6
 */

@Service
public class ObjectStorageService {
    private final ObjectStorageFactory factory;

    public ObjectStorageService(ObjectStorageFactory factory) {
        this.factory = factory;
    }

    public String uploadFile(ObjectStoragePlatform platform, String fileName, String filePath, InputStream inputStream) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform,fileName, filePath, inputStream,expiration);
    }

    public String uploadFile(ObjectStoragePlatform platform, String fileName, String filePath, InputStream inputStream , Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.uploadFile(fileName, filePath, inputStream,expiration);
    }

    public String getSignedUrl(ObjectStoragePlatform platform, String fileName, String filePath, Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.getSignedUrl(fileName, filePath, expiration);
    }
}
