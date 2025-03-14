package cloud.huazai.objectstorage.properties;

import cloud.huazai.objectstorage.constant.ObjectStorageConstant;
import cloud.huazai.tool.java.constant.StringConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ObjectStorageProperties
 *
 * @author devon
 * @since 2025/1/21
 */

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = ObjectStorageConstant.HUA_ZAI+ StringConstant.DOT+ObjectStorageConstant.OBJECT_STORAGE)
public class ObjectStorageProperties {

    private Map<String, ObjectStoragePlatformProperties> platforms;

}
