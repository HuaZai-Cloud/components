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

    public void uploadFile(String platformName, String fileName, byte[] fileContent) {
        ObjectStorageClient client = factory.getClient(platformName);
        client.uploadFile(fileName, fileContent);
    }

    public byte[] downloadFile(String platformName, String fileName) {
        ObjectStorageClient client = factory.getClient(platformName);
        return client.downloadFile(fileName);
    }

    public String uploadFile(String platformName, String fileName, InputStream inputStream) {
        ObjectStorageClient client = factory.getClient(platformName);
        return client.uploadFile(fileName, , inputStream);
    }
}
