package cloud.huazai.operationlog.aspect;


import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    // 定义切点：拦截所有使用了 @OperationLog 注解的方法
    @Pointcut("@annotation(cloud.huazai.operationlog.annotation.OperationLog)")
    public void operationLogMethods() {}

    // 请求前日志记录
    @Before("operationLogMethods()")
    public void logRequest(JoinPoint joinPoint) throws NoSuchMethodException {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

       String params = getRequestParams(joinPoint);

        logger.info("[OperationLog] [Request] {} {}", packageNameAndMethodName, StringUtils.isNotBlank(params) ? params : "");
    }




    // 请求成功后的日志记录
    @AfterReturning(value = "operationLogMethods()", returning = "response")
    public void logResponse(JoinPoint joinPoint, Object response) {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

       String responseInfo = getResponseInfo(response);

        logger.info("[OperationLog] [Response] {} {}", packageNameAndMethodName, StringUtils.isNotBlank(responseInfo) ? responseInfo : "");
    }



    // 请求发生异常时的日志记录
    @AfterThrowing(value = "operationLogMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

        if (!logger.isInfoEnabled()) {
            String params = getRequestParams(joinPoint);
            logger.error("[OperationLog] [Request] {} {}", packageNameAndMethodName, StringUtils.isNotBlank(params) ? params : "");
        }

        logger.error("[OperationLog] [Exception] {} {}", packageNameAndMethodName, ex.getMessage());
    }


    private String packageNameAndMethodName(JoinPoint joinPoint) {
        String packageName = joinPoint.getTarget().getClass().getPackage().getName();
        String shortString = joinPoint.getSignature().toShortString();
        return packageName + "." + shortString;
    }

    private String getRequestParams(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        if (joinPoint.getArgs() == null || joinPoint.getArgs().length == 0) {
            return null;
        }

        List<String> paramList = new ArrayList<>();
        for (Object arg : args) {
            paramList.add(JSON.toJSONString(arg, JSONWriter.Feature.IgnoreErrorGetter));
        }
        if (CollectionUtils.isEmpty(paramList)) {
            return null;
        }
        return String.join(",", paramList);
    }

    private String getResponseInfo(Object response) {
        if (response == null) {
            return null;
        }

        return JSON.toJSONString(response);
    }
}
