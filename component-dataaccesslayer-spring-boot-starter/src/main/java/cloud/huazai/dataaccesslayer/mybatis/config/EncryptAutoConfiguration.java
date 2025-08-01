package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.crypto.CryptoHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.AesCryptoHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.EncryptFieldInterceptor;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
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
public class EncryptAutoConfiguration {


    // 默认的 CryptoHandler，只有当用户没提供时才创建
    @Bean
    @ConditionalOnMissingBean(CryptoHandler.class)
    public CryptoHandler aesCryptoHandler(EncryptProperties properties) throws Exception {
        return new AesCryptoHandler(properties);
    }

    // 创建拦截器，Spring 会自动注入合适的 CryptoHandler
    @Bean
    public EncryptFieldInterceptor encryptFieldInterceptor(CryptoHandler cryptoHandler) {
        return new EncryptFieldInterceptor(cryptoHandler);
    }

    // 注册到 MyBatis Plus 拦截器链
    @Bean
    public MybatisPlusInterceptor myBatisPlusInterceptor(EncryptFieldInterceptor encryptFieldInterceptor) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(encryptFieldInterceptor);
        return interceptor;
    }
}
