package cloud.huazai.objectstorage.ali;

import cloud.huazai.objectstorage.constant.ObjectStorageConstant;
import cloud.huazai.objectstorage.constant.ObjectStorageType;
import cloud.huazai.objectstorage.object.ObjectStorageClient;
import cloud.huazai.objectstorage.object.ObjectStorageConfig;
import cloud.huazai.tool.java.constant.StringConstant;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;

import io.github.artislong.core.ali.model.AliOssClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Optional;

/**
 * AliOssAutoConfiguration
 *
 * @author devon
 * @since 2025/1/23
 */


@SpringBootConfiguration
@ConditionalOnClass(OSSClient.class)
@EnableConfigurationProperties({AliOssProperties.class})
@ConditionalOnProperty(prefix = ObjectStorageConstant.HUA_ZAI+ StringConstant.DOT+ObjectStorageConstant.OBJECT_STORAGE+StringConstant.DOT+ ObjectStorageType.ALI)
public class AliOssAutoConfiguration {

    public static final String DEFAULT_BEAN_NAME = "aliOssClient";

    @Autowired
    private AliOssProperties aliOssProperties;

    @Bean
    public ObjectStorageClient aliOssClient() {
        Map<String, ObjectStorageConfig> aliOssConfigMap = aliOssProperties.getOssConfig();
        if (aliOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, aliOssClient(aliOssProperties));
        } else {
            String endpoint = aliOssProperties.getEndpoint();
            String accessKey = aliOssProperties.getAccessKey();
            String accessSecret = aliOssProperties.getAccessSecret();
            String bucketName = aliOssProperties.getBucketName();
            aliOssConfigMap.forEach((name, aliOssConfig) -> {
                if (ObjectUtil.isEmpty(aliOssConfig.getEndpoint())) {
                    aliOssConfig.setEndpoint(endpoint);
                }
                if (ObjectUtil.isEmpty(aliOssConfig.getAccessKey())) {
                    aliOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(aliOssConfig.getAccessSecret())) {
                    aliOssConfig.setAccessSecret(accessSecret);
                }
                if (ObjectUtil.isEmpty(aliOssConfig.getBucketName())) {
                    aliOssConfig.setBucketName(bucketName);
                }

                SpringUtil.registerBean(name, aliOssClient(aliOssConfig));
            });
        }
        return null;
    }

    public ObjectStorageClient aliOssClient(ObjectStorageConfig aliOssConfig) {
        return new AliOssClient(ossClient(aliOssConfig), aliOssConfig);
    }

    public OSS ossClient(ObjectStorageConfig aliOssConfig) {

        return new OSSClientBuilder().build(aliOssConfig.getEndpoint(),  aliOssConfig.getAccessKey(), aliOssConfig.getAccessSecret());



    }

}
