package cloud.huazai.objectstorage.core;

import cloud.huazai.objectstorage.constant.ObjectStorageType;
import cloud.huazai.objectstorage.constant.PlatformType;
import cloud.huazai.objectstorage.platform.AliOssClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import cloud.huazai.objectstorage.properties.ObjectStorageProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectStorageFactory
 *
 * @author devon
 * @since 2025/2/6
 */

@Component
public class ObjectStorageFactory {
    private final Map<String, ObjectStorageClient> clients = new HashMap<>();

    public ObjectStorageFactory(ObjectStorageProperties properties) {
        for (Map.Entry<String, ObjectStoragePlatformProperties> entry : properties.getPlatforms().entrySet()) {
            String platformName = entry.getKey();
            ObjectStoragePlatformProperties platformProperties = entry.getValue();
            ObjectStorageClient client = createClient(platformProperties);
            clients.put(platformName, client);
        }
    }

    private ObjectStorageClient createClient(ObjectStoragePlatformProperties properties) {
        return switch (properties.getType().toLowerCase()) {
            case ObjectStorageType.ALI -> new AliOssClient(properties);
            // case ObjectStorageType.TENCENT -> new TencentCosClient(properties);


            default -> throw new IllegalArgumentException("Unsupported platform type: " + properties.getType());
        };
    }

    public ObjectStorageClient getClient(String platformName) {
        return clients.get(platformName);
    }
}
