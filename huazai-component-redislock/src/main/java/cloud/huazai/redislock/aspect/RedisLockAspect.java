package cloud.huazai.redislock.aspect;

import cloud.huazai.redislock.annotation.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * RedisLockAspect
 *
 * @author huazai
 * @since 2024/12/20
 */

@Aspect
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();

    public RedisLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(lockAnnotation)")
    public Object handleDistributedLock(ProceedingJoinPoint joinPoint, RedisLock lockAnnotation) throws Throwable {
        String baseKey = lockAnnotation.key();
        String[] keySuffix = lockAnnotation.keySuffix();
        long leaseTime = lockAnnotation.leaseTime();
        long waitTime = lockAnnotation.waitTime();
        String errorMessage = lockAnnotation.errorMessage();

        // 解析完整的 Key
        String lockKey = buildLockKey(baseKey, keySuffix, joinPoint);

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                throw new IllegalStateException(errorMessage + ": " + lockKey);
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String buildLockKey(String baseKey, String[] keySuffixArray, ProceedingJoinPoint joinPoint) {
        if (keySuffixArray == null || keySuffixArray.length == 0) {
            return baseKey;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        StringBuilder suffixBuilder = new StringBuilder();
        for (String suffixPart : keySuffixArray) {
            Object resolvedValue = parser.parseExpression(suffixPart).getValue(context);
            suffixBuilder.append(resolvedValue).append(":");
        }

        if (!suffixBuilder.isEmpty()) {
            suffixBuilder.setLength(suffixBuilder.length() - 1);
        }

        return baseKey + ":" + suffixBuilder.toString();
    }




}
