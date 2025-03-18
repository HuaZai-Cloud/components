package cloud.huazai.objectstorage.config;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.core.ObjectStorageService;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ObjectStorageClientShutdownManager {

    @Resource
    private ObjectStorageService objectStorageService;

    @PreDestroy
    public void shutdownOssClient() {
        for (ObjectStoragePlatform platform : ObjectStoragePlatform.values()) {
            objectStorageService.shutdownObjectStorageClient(platform);
        }
    }

}
