package cloud.huazai.objectstorage.core;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import cloud.huazai.objectstorage.platform.PlatformClientFactory;
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
    private final Map<ObjectStoragePlatform, ObjectStorageClient> clients = new HashMap<>();

    public ObjectStorageFactory(ObjectStorageProperties properties) {
        for (Map.Entry<String, ObjectStoragePlatformProperties> entry : properties.getPlatforms().entrySet()) {
            String platformName = entry.getKey();
            ObjectStoragePlatformProperties platformProperties = entry.getValue();
            ObjectStoragePlatform platform = ObjectStoragePlatform.fromString(platformName);
            ObjectStorageClient client = PlatformClientFactory.createClient(platform,platformProperties);
            clients.put(platform, client);
        }
    }


    public ObjectStorageClient getClient(ObjectStoragePlatform platform) {
        return clients.get(platform);
    }
}
