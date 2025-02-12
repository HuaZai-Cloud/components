package cloud.huazai.objectstorage.config;

import cloud.huazai.objectstorage.core.ObjectStorageFactory;
import cloud.huazai.objectstorage.core.ObjectStorageService;
import cloud.huazai.objectstorage.properties.ObjectStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ObjectStorageAutoConfiguration
 *
 * @author devon
 * @since 2025/1/22
 */


@Configuration
@EnableConfigurationProperties(ObjectStorageProperties.class)
public class ObjectStorageAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ObjectStorageFactory objectStorageFactory(ObjectStorageProperties properties) {

        System.out.println("测试推送");
        return new ObjectStorageFactory(properties);



    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectStorageService objectStorageService(ObjectStorageFactory factory) {
        return new ObjectStorageService(factory);
    }

}
