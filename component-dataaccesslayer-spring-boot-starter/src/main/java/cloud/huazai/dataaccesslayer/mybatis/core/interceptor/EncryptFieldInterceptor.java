package cloud.huazai.dataaccesslayer.mybatis.core.interceptor;

import cloud.huazai.dataaccesslayer.mybatis.annotation.Encrypt;
import cloud.huazai.dataaccesslayer.mybatis.core.encrypt.EncryptHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * EncryptFieldInterceptor
 *
 * @author Devon
 * @since 2025/8/1 12:00
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class EncryptFieldInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(EncryptFieldInterceptor.class);

    private final EncryptHandler encryptHandler;

    public EncryptFieldInterceptor(EncryptHandler encryptHandler) {
        if (encryptHandler == null) {
            throw new IllegalArgumentException("CryptoHandler must not be null");
        }
        this.encryptHandler = encryptHandler;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            String methodName = invocation.getMethod().getName();

            if ("update".equals(methodName)) {
                // 更新操作前加密参数
                Object[] args = invocation.getArgs();
                Object parameter = args[1];
                if (parameter != null) {
                    encryptObject(parameter);
                }
            } else if ("query".equals(methodName)) {
                // 先执行查询
                Object result = invocation.proceed();

                // 查询结果解密
                if (result instanceof List) {
                    // 处理查询结果列表
                    List<?> resultList = (List<?>) result;
                    for (Object item : resultList) {
                        if (item != null) {
                            try {
                                decryptObject(item);
                            } catch (Exception e) {
                                // 记录解密失败日志，但不中断处理流程
                                logger.warn("解密对象失败，跳过解密: {}", e.getMessage());
                            }
                        }
                    }
                } else if (result != null) {
                    // 处理单个查询结果
                    try {
                        decryptObject(result);
                    } catch (Exception e) {
                        // 记录解密失败日志，但不中断处理流程
                        logger.warn("解密对象失败，跳过解密: {}", e.getMessage());
                    }
                }

                return result;
            }

            return invocation.proceed();
        } catch (Exception e) {
            // 记录详细错误信息
            logger.error("加密拦截器处理异常: ", e);
            throw e; // 重新抛出异常，让上层处理
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里设置插件属性
    }

    private void encryptObject(Object obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();

        for (Field field : getAllFields(clazz)) {
            if (field.getType() == String.class && field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null && !isEncrypted(value)) {
                        String encrypted = encryptHandler.encrypt(value);
                        field.set(obj, "ENC(" + encrypted + ")");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("加密失败: " + field.getName(), e);
                }
            }
        }
    }

    private void decryptObject(Object obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();

        for (Field field : getAllFields(clazz)) {
            if (field.getType() == String.class && field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null && isEncrypted(value)) {
                        String ciphertext = value.substring(4, value.length() - 1); // 去掉 ENC( 和 )
                        String decrypted = encryptHandler.decrypt(ciphertext);
                        field.set(obj, decrypted);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("解密失败: " + field.getName(), e);
                }
            }
        }
    }

    // 辅助方法获取所有字段（包括父类）
    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    // 判断是否已经是加密后的数据
    private boolean isEncrypted(String value) {
        return value != null && value.startsWith("ENC(") && value.endsWith(")");
    }


}
