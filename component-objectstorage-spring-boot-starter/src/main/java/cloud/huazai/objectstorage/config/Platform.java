package cloud.huazai.objectstorage.config;

import lombok.Getter;

/**
 * Platform
 *
 * @author devon
 * @since 2025/1/21
 */

@Getter
public enum Platform {

    OSS("oss"),
    COS("cos"),
    TOS("tos"),

    ;
    private final String platform;

    Platform(String platform) {
        this.platform = platform;
    }

}
