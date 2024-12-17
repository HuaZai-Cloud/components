package cloud.huazai.operationlog.aspect;


import cloud.huazai.operationlog.annotation.OperationLog;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Aspect
@Order(1)
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    // 定义切点：拦截所有使用了 @OperationLog 注解的方法
    @Pointcut("@annotation(cloud.huazai.operationlog.annotation.OperationLog)")
    public void operationLogMethods() {
    }

    // 请求前日志记录
    @Before("operationLogMethods()")
    public void logRequest(JoinPoint joinPoint) throws NoSuchMethodException {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

       String params = getParams(joinPoint);

        logger.info("[OperationLog] [Request] {} {}", packageNameAndMethodName, StringUtils.isNotBlank(params) ? params : "");
    }




    // 请求成功后的日志记录
    @AfterReturning(value = "operationLogMethods()", returning = "response")
    public void logResponse(JoinPoint joinPoint, Object response) throws NoSuchMethodException {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

       String result = getResult(response);

        logger.info("[OperationLog] [Response] {} {}", packageNameAndMethodName, StringUtils.isNotBlank(result) ? result : "");
    }



    // 请求发生异常时的日志记录
    @AfterThrowing(value = "operationLogMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) throws NoSuchMethodException {

        String packageNameAndMethodName = packageNameAndMethodName(joinPoint);

        logger.error("[OperationLog] [Exception] {} {}", packageNameAndMethodName, ex.getMessage());
    }

    // 辅助方法：获取当前方法
    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.getMethod(methodName, toClassArray(joinPoint.getArgs()));
    }

    // 辅助方法：根据参数类型获取方法
    private Class<?>[] toClassArray(Object[] args) {
        Class<?>[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }

    private String packageNameAndMethodName(JoinPoint joinPoint) {
        String packageName = joinPoint.getTarget().getClass().getPackage().getName();
        String shortString = joinPoint.getSignature().toShortString();
        return packageName + "." + shortString;
    }

    private String getParams(JoinPoint joinPoint) {

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

    private String getResult(Object response) {
        if (response == null) {
            return null;
        }

        return JSON.toJSONString(response);
    }
}
