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
    private final String platform;

    PlatformType(String platform) {
        this.platform = platform;
    }

}
