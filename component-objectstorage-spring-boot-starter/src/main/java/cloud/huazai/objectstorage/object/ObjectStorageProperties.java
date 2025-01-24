package cloud.huazai.objectstorage.object;

import io.github.artislong.core.aws.model.AwsOssConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectStorageProperties
 *
 * @author devon
 * @since 2025/1/23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ObjectStorageProperties extends ObjectStorageConfig  {

    private Boolean enable = false;

    private Map<String, ObjectStorageConfig> ossConfig = new HashMap<>();


}
