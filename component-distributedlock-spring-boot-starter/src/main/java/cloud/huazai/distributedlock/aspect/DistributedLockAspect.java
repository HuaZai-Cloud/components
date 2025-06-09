package cloud.huazai.distributedlock.aspect;

import cloud.huazai.distributedlock.annotation.DistributedLock;
import cloud.huazai.distributedlock.exception.DistributedLockException;
import cloud.huazai.tool.java.constant.StringConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RedisLockAspect
 *
 * @author huazai
 * @since 2024/12/20
 */

@Aspect
@Component
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_EXCEPTION_INFO = "[RedissonLock] [Exception] {} {} {}";
    private static final String DEFAULT_BASE_KEY = "distributed_lock";

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(lockAnnotation)")
    public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) throws Throwable {
        String baseKey = lockAnnotation.key();
        String[] keySuffix = lockAnnotation.keySuffix();
        long leaseTime = lockAnnotation.leaseTime();
        long waitTime = lockAnnotation.waitTime();
        String errorMessage = lockAnnotation.errorMessage();

        // 检查是否为异步方法，避免锁释放问题
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method.isAnnotationPresent(Async.class)) {
            throw new IllegalStateException("DistributedLock aspect is not supported for async methods.");
        }


        // 解析完整的 Key
        String lockKey = buildLockKey(baseKey, keySuffix, joinPoint);

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                String packageNameAndMethodName = getPackageNameAndMethodName(joinPoint);
                LOGGER.error(REDISSON_LOCK_EXCEPTION_INFO, packageNameAndMethodName, lockKey, errorMessage);
                throw new DistributedLockException(errorMessage);
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DistributedLockException("Interrupted while waiting for the lock", e);
        } finally {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                LOGGER.error("Failed to release lock: {}", lockKey, e);
            }
        }
    }

    private String buildLockKey(String baseKey, String[] keySuffixArray, ProceedingJoinPoint joinPoint) {

        if (keySuffixArray == null || keySuffixArray.length == 0) {
            return baseKey.isEmpty() ? DEFAULT_BASE_KEY : baseKey;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        StringBuilder suffixBuilder = new StringBuilder();
        for (String suffixPart : keySuffixArray) {
            // 使用缓存的表达式，避免重复解析
            try {
                Expression expression = expressionCache.computeIfAbsent(suffixPart, parser::parseExpression);
                Object resolvedValue = expression.getValue(context);
                suffixBuilder.append(resolvedValue).append(StringConstant.COLON);
            } catch (EvaluationException e) {
                throw new IllegalArgumentException("Failed to resolve SpEL expression: " + suffixPart, e);
            }
        }

        if (!suffixBuilder.isEmpty()) {
            suffixBuilder.setLength(suffixBuilder.length() - 1);
        }

        return (baseKey.isEmpty() ? DEFAULT_BASE_KEY : baseKey) + StringConstant.COLON + suffixBuilder;
    }

    private String getPackageNameAndMethodName(JoinPoint joinPoint) {
        String packageName = Optional.ofNullable(joinPoint.getTarget().getClass().getPackage())
                .map(Package::getName)
                .orElse(StringConstant.BLANK);
        String shortString = joinPoint.getSignature().toShortString();
        return packageName + StringConstant.DOT + shortString;
    }

}
