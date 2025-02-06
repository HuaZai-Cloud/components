package cloud.huazai.objectstorage.core;

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

    public String uploadFile(String platformName, String fileName,String filePath, InputStream inputStream) {
        ObjectStorageClient client = factory.getClient(platformName);
        return client.uploadFile(fileName, filePath, inputStream);
    }
}
