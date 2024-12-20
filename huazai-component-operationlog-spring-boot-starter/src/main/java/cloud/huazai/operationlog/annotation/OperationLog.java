package cloud.huazai.operationlog.annotation;

import java.lang.annotation.*;

/**
 * OperationLog
 *
 * @author devon
 * @since 2024/12/12
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

}
