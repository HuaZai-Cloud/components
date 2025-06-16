package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;

/**
 * PlatformClientFactory
 *
 * @author Devon
 * @since 2025/6/16 13:33
 */

public class PlatformClientFactory {

   public static ObjectStorageClient createClient(ObjectStoragePlatform platform, ObjectStoragePlatformProperties properties) {
           return switch (platform) {
               case ALI -> new AliOssClient(properties);
               case BYTEDANCE -> new ByteDanceTosClient(properties);

               default -> throw new IllegalArgumentException("Unsupported platform type: " + platform.name());
           };
       }

}
