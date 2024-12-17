package cloud.huazai.operationlog.config;

import cloud.huazai.operationlog.aspect.OperationLogAspect;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OperationLogConfiguration
 *
 * @author devon
 * @since 2024/12/12
 */
@Configuration
public class OperationLogAutoConfiguration {

    @Bean
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }
}
