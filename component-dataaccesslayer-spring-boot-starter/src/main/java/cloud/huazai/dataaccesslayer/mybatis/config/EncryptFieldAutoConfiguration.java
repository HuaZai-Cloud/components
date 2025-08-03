package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.encrypt.EncryptHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.AesEncryptHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.EncryptFieldInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EncryptAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/1 12:03
 */
@AutoConfiguration
@ConditionalOnClass(SqlSessionFactory.class)
@EnableConfigurationProperties(EncryptProperties.class)
public class EncryptFieldAutoConfiguration {


    // 默认的 CryptoHandler，只有当用户没提供时才创建
    @Bean
    @ConditionalOnMissingBean(EncryptHandler.class)
    public EncryptHandler aesCryptoHandler(EncryptProperties properties) throws Exception {
        return new AesEncryptHandler(properties);
    }

    // 创建拦截器，Spring 会自动注入合适的 CryptoHandler
    @Bean
    public EncryptFieldInterceptor encryptFieldInterceptor(EncryptHandler encryptHandler) {
        return new EncryptFieldInterceptor(encryptHandler);
    }
}
