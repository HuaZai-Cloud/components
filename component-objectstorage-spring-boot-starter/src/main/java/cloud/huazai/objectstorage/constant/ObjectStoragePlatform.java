package cloud.huazai.objectstorage.constant;

import lombok.Getter;

/**
 * Platform
 *
 * @author devon
 * @since 2025/1/21
 */

@Getter
public enum ObjectStoragePlatform {

    ALI,
    TENCENT,


    ;
    public static ObjectStoragePlatform fromString(String platformName) {
        for (ObjectStoragePlatform platform : ObjectStoragePlatform.values()) {
            if (platform.name().equalsIgnoreCase(platformName)) {
                return platform;
            }
        }
        throw new IllegalArgumentException("Platform '" + platformName + "' is not supported");
    }

}
