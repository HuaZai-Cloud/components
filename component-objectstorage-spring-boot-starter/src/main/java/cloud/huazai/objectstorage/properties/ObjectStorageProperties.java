package cloud.huazai.objectstorage.properties;

import cloud.huazai.objectstorage.constant.ObjectStorageConstant;
import cloud.huazai.tool.java.constant.StringConstant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectStorageProperties
 *
 * @author devon
 * @since 2025/1/21
 */

@Getter
@ConfigurationProperties(prefix = ObjectStorageConstant.HUA_ZAI+ StringConstant.DOT+ObjectStorageConstant.OBJECT_STORAGE)
public class ObjectStorageProperties {


    private Map<String, ObjectStoragePlatformProperties> platforms;

    public void setPlatforms(Map<String, ObjectStoragePlatformProperties> platforms) {
        this.platforms = platforms;
    }
}
