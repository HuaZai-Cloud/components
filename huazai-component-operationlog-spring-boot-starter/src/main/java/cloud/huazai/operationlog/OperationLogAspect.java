package cloud.huazai.operationlog;

import cloud.huazai.exception.BusinessException;
import com.alibaba.cola.catchlog.ResponseHandlerFactory;
import com.alibaba.cola.exception.BaseException;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.exception.SysException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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

    public OperationLogAspect(OperationLogProperties properties) {
        this.properties = properties;
    }

    @Around("componentMethods()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("开始执行方法: {}，参数: {}", methodName, args);

        Object result;
        try {
            result = joinPoint.proceed();
            logger.info("方法 {} 执行成功，返回值: {}", methodName, result);
        } catch (Throwable ex) {
            logger.error("方法 {} 执行异常: {}", methodName, ex.getMessage());
            throw ex;
        }

        return result;
    }

    @Pointcut("@within(CatchAndLog) && execution(public * *(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        this.logRequest(joinPoint);
        Object response = null;

        try {
            response = joinPoint.proceed();
        } catch (Throwable var9) {
            response = this.handleException(joinPoint, var9);
        } finally {
            this.logResponse(startTime, response);
        }

        return response;
    }



    @Pointcut("execution(public * *(..)) && (controllerMethods() || serviceMethods() || repositoryMethods())")
    public void componentMethods() {}

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



    private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        Class<?> returnType = ms.getReturnType();
        if (e instanceof BusinessException) {
            logger.warn("BIZ EXCEPTION : {}", e.getMessage());
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return ResponseHandlerFactory.get().handle(returnType, ((BizException)e).getErrCode(), e.getMessage());
        } else if (e instanceof SysException) {
            logger.error("SYS EXCEPTION: {}", e.getMessage(), e);
            return ResponseHandlerFactory.get().handle(returnType, ((SysException)e).getErrCode(), e.getMessage());
        } else {
            logger.error("UNKNOWN EXCEPTION: {}", e.getMessage(), e);
            return ResponseHandlerFactory.get().handle(returnType, "UNKNOWN_ERROR", e.getMessage());
        }
    }

    private void logResponse(long startTime, Object response) {
        try {
            long endTime = System.currentTimeMillis();
            if (logger.isDebugEnabled()) {
                logger.debug("RESPONSE: {}", JSON.toJSONString(response));
                logger.debug("COST: {}ms", endTime - startTime);
            }
        } catch (Exception var6) {
            logger.error("logResponse error: {}", var6.getMessage(), var6);
        }

    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        if (logger.isDebugEnabled()) {
            try {
                logger.debug("START PROCESSING: {}", joinPoint.getSignature().toShortString());
                Object[] args = joinPoint.getArgs();
                Object[] var3 = args;
                int var4 = args.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Object arg = var3[var5];
                    logger.debug("REQUEST: {}", logger.toJSONString(arg, new SerializerFeature[]{SerializerFeature.IgnoreErrorGetter}));
                }
            } catch (Exception var7) {
                logger.error("logReqeust error: {}", var7.getMessage(), var7);
            }

        }
    }
}
