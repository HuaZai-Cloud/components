package cloud.huazai.objectstorage.core;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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
        ObjectStorageClient client = factory.getClient(platform);
        return client.uploadFile(fileName, filePath, inputStream);
    }
}
