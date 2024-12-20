package cloud.huazai.redislock.annotation;

import java.lang.annotation.*;
import java.util.ArrayList;

/**
 * RedisLock
 *
 * @author huazai
 * @since 2024/12/20
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    String key(); // 基础 Key（支持固定值或部分 SpEL 表达式）
    String[] keySuffix() default {}; // 锁后缀（列表形式）（支持 SpEL 表达式）
    long leaseTime() default 30; // 锁的自动释放时间（秒）
    long waitTime() default 5; // 尝试获取锁的最大等待时间（秒）
    String errorMessage() default "Unable to acquire lock"; // 未获取锁时的异常信息




}
