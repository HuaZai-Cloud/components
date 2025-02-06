package cloud.huazai.objectstorage.constant;

import lombok.Getter;

/**
 * Platform
 *
 * @author devon
 * @since 2025/1/21
 */

@Getter
public enum PlatformType {

    ALI(ObjectStorageType.ALI),
    TENCENT(ObjectStorageType.TENCENT),


    ;
    private final String platformName;

    PlatformType(String platformName) {
        this.platformName = platformName;
    }

}
