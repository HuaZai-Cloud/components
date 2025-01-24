package cloud.huazai.objectstorage.config;

import cloud.huazai.objectstorage.constant.PlatformType;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ObjectStorageAutoConfiguration
 *
 * @author devon
 * @since 2025/1/22
 */

// @Configuration
// @EnableConfigurationProperties(ObjectStorageProperties.class)
public class ObjectStorageAutoConfiguration {

    private final ObjectStorageProperties properties;

    public ObjectStorageAutoConfiguration(ObjectStorageProperties properties) {
        this.properties = properties;
    }

    // 根据不同的平台名称动态创建客户端
    @Bean
    public OSS aliyunOSSClient() {
        ObjectStorageProperties.ProviderProperties providerProperties = properties.getProviders().get(PlatformType.ALI.getPlatform());
        return new OSSClientBuilder().build(providerProperties.getEndpoint(), providerProperties.getAccessKey(), providerProperties.getSecretKey());
    }

    // @Bean
    // public COSClient tencentCOSClient() {
    //     StorageConfig tencentConfig = properties.getObjectstorage().get("tencent");
    //     Region region = new Region(tencentConfig.getEndpoint());
    //     return new COSClient(new com.qcloud.cos.auth.COSCredentials(tencentConfig.getAccessKey(), tencentConfig.getAccessSecret()), region);
    // }
}
