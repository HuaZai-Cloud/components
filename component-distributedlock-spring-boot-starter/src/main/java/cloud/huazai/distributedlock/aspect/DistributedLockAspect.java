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
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();

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

        // 解析完整的 Key
        String lockKey = buildLockKey(baseKey, keySuffix, joinPoint);

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                String packageNameAndMethodName = getPackageNameAndMethodName(joinPoint);
                LOGGER.error(REDISSON_LOCK_EXCEPTION_INFO, packageNameAndMethodName,lockKey, errorMessage);
                throw new DistributedLockException(errorMessage);
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
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        StringBuilder suffixBuilder = new StringBuilder();
        for (String suffixPart : keySuffixArray) {
            Object resolvedValue = parser.parseExpression(suffixPart).getValue(context);
            suffixBuilder.append(resolvedValue).append(StringConstant.COLON);
        }

        if (!suffixBuilder.isEmpty()) {
            suffixBuilder.setLength(suffixBuilder.length() - 1);
        }

        return baseKey + StringConstant.COLON + suffixBuilder.toString();
    }

    private String getPackageNameAndMethodName(JoinPoint joinPoint) {
        String packageName = joinPoint.getTarget().getClass().getPackage().getName();
        String shortString = joinPoint.getSignature().toShortString();
        return packageName + StringConstant.DOT + shortString;
    }

}
