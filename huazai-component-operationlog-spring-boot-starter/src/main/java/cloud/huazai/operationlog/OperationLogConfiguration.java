package cloud.huazai.operationlog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;

/**
 * OperationLogConfiguration
 *
 * @author devon
 * @since 2024/12/12
 */
@Configuration
public class OperationLogConfiguration {

    @Bean
    public LogProperties logProperties() {
        return new LogProperties();
    }

    @Bean
    public OperationLogProperties operationLogProperties() {
        return new OperationLogProperties();
    }

    @Bean
    public OperationLogAspect operationLogAspect(OperationLogProperties properties) {
        return new OperationLogAspect(properties);
    }

    @Bean
    public void configureOperationLogProperties(OperationLogProperties properties) {
        // 获取启动类上的 @EnableOperationLog 注解
        Class<?> mainApplicationClass = findMainApplicationClass();
        EnableOperationLog annotation = AnnotationUtils.findAnnotation(mainApplicationClass, EnableOperationLog.class);

        if (annotation != null) {
            properties.setInclude(Arrays.asList(annotation.include()));
        }
    }

    private Class<?> findMainApplicationClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            try {
                Class<?> clazz = Class.forName(element.getClassName());
                if (clazz.getAnnotation(SpringBootApplication.class) != null) {
                    return clazz;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new IllegalStateException("Main application class not found.");
    }
}
