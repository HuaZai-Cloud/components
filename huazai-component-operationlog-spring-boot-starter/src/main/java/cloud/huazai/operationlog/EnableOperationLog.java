package cloud.huazai.operationlog;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableOperationLog
 *
 * @author devon
 * @since 2024/12/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OperationLogProperties.class) // 引入日志记录相关配置
public @interface EnableOperationLog {

    /**
     * 指定需要记录日志的组件类型
     * 可选值：Controller, Service, Repository
     */
    String[] include() default {"Controller", "Service", "Repository"};
}
