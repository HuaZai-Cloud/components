package cloud.huazai.objectstorage.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Platform
 *
 * @author devon
 * @since 2025/1/21
 */

@Getter
public enum ObjectStoragePlatform {

    ALI,
    BYTEDANCE,

    ;
    public static ObjectStoragePlatform fromString(String platformName) {
        for (ObjectStoragePlatform platform : ObjectStoragePlatform.values()) {
            if (platform.name().equalsIgnoreCase(platformName)) {
                return platform;
            }
        }
        String collect = Arrays.stream(ObjectStoragePlatform.values())
                .map(f -> f.name().toLowerCase())
                .collect(Collectors.joining("、"));
        throw new IllegalArgumentException("Unsupported Platform type: " + platformName
                + ". Supported types are " + collect);
    }

}
