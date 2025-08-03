package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.JsonFieldInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JsonFieldAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/2 16:44
 */
@Configuration
@AutoConfiguration
public class JsonFieldAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JsonFieldInterceptor.class)
    public JsonFieldInterceptor jsonFieldInterceptor(ObjectProvider<ObjectMapper> objectMapperProvider) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable();
        return new JsonFieldInterceptor(objectMapper);
    }
}
