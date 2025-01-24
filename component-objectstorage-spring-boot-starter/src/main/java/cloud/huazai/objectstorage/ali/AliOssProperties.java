package cloud.huazai.objectstorage.ali;

import cloud.huazai.objectstorage.constant.ObjectStorageConstant;
import cloud.huazai.objectstorage.constant.ObjectStorageType;
import cloud.huazai.objectstorage.constant.PlatformType;
import cloud.huazai.objectstorage.object.ObjectStorageProperties;
import cloud.huazai.tool.java.constant.StringConstant;
import cn.hutool.core.text.CharPool;
import io.github.artislong.constant.OssConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AliOssProperties
 *
 * @author devon
 * @since 2025/1/23
 */


@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(ObjectStorageConstant.HUA_ZAI+StringConstant.DOT+ObjectStorageConstant.OBJECT_STORAGE+StringConstant.DOT+ ObjectStorageType.ALI)
public class AliOssProperties extends ObjectStorageProperties {


}
