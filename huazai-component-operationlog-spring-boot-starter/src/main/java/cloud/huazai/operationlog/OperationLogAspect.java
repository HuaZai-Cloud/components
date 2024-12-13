package cloud.huazai.operationlog;

import cloud.huazai.exception.BaseException;
import cloud.huazai.exception.BusinessException;
import cloud.huazai.exception.SysException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;



/**
 * OperationLogAspect
 *
 * @author devon
 * @since 2024/12/12
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    private final OperationLogProperties properties;

    // 使用 ThreadLocal 避免重复记录日志
    private static final ThreadLocal<Boolean> alreadyLogged = ThreadLocal.withInitial(() -> false);


    public OperationLogAspect(OperationLogProperties properties) {
        this.properties = properties;
    }

    @Around("enableOperationLogMethods() || operationLogMethods()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {

        if (alreadyLogged.get()) {
            return joinPoint.proceed(); // 如果已经记录过日志，直接执行方法
        }

        try {
            alreadyLogged.set(true); // 设置标记，表示当前线程已记录日志

            long startTime = System.currentTimeMillis();
            // String methodName = joinPoint.getSignature().getName();
            // Object[] args = joinPoint.getArgs();

            loggerRequest(joinPoint);
            Object response = null;
            try {
                response = joinPoint.proceed();
            } catch (Throwable throwable) {
                loggerException(joinPoint, throwable);
                throw throwable;
            } finally {
                loggerResponse( startTime,  response);
            }

            return response;

        } finally {
            alreadyLogged.remove(); // 清理标记

        }

    }




    // 记录 @OperationLog 标记的方法日志
    // @Pointcut("@annotation(cloud.huazai.operationlog.OperationLog)")
    // public void operationLogMethods() {}

    @Pointcut("@within(OperationLog) && execution(public * *(..))")
    public void operationLogMethods() {}




    @Pointcut("execution(public * *(..)) && (controllerMethods() || serviceMethods() || repositoryMethods())")
    public void enableOperationLogMethods() {}

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public boolean controllerMethods() {
        return isIncluded("Controller");
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public boolean serviceMethods() {
        return isIncluded("Service");
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public boolean repositoryMethods() {
        return isIncluded("Repository");
    }

    private boolean isIncluded(String type) {
        List<String> include = properties.getInclude();
        return include != null && include.contains(type);
    }





    private void loggerException(ProceedingJoinPoint joinPoint, Throwable e) {

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 进入异常首先记录请求参数
        logger.error("START PROCESSING: {}",methodName);
        for (Object arg : args) {
            logger.error("REQUEST: {}", JSON.toJSONString(arg, JSONWriter.Feature.IgnoreErrorGetter));
        }


        switch (e) {
            case BusinessException businessException -> {
                logger.warn("BIZ EXCEPTION : {}", e.getMessage());
                if (logger.isDebugEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
            case SysException sysException -> logger.error("SYS EXCEPTION: {}", e.getMessage(), e);
            case BaseException baseException -> logger.error("Base EXCEPTION: {}", e.getMessage(), e);
            case null, default -> logger.error("UNKNOWN EXCEPTION: {}", (e!= null? e.getMessage() : ""), e);
        }

    }


    private void loggerResponse(long startTime, Object response) {
        try {
            long endTime = System.currentTimeMillis();
            if (logger.isDebugEnabled()) {
                logger.debug("RESPONSE: {}", JSON.toJSONString(response));
                logger.debug("COST: {}ms", endTime - startTime);
            }
        } catch (Exception e) {
            logger.error("logResponse error: {}", e.getMessage(), e);
        }


    }



    private void loggerRequest(ProceedingJoinPoint joinPoint) {

        if (logger.isDebugEnabled()) {

            try {
                String methodName = joinPoint.getSignature().getName();
                Object[] args = joinPoint.getArgs();

                logger.debug("START PROCESSING: {}",methodName);

                for (Object arg : args) {
                    logger.debug("REQUEST: {}", JSON.toJSONString(arg, JSONWriter.Feature.IgnoreErrorGetter));
                }
            } catch (Exception e) {
                logger.error("logReqeust error: {}", e.getMessage(), e);
            }
        }

    }
}
