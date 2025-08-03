package cloud.huazai.dataaccesslayer.mybatis.core.interceptor;

import cloud.huazai.dataaccesslayer.mybatis.annotation.JsonCollection;
import cloud.huazai.dataaccesslayer.mybatis.annotation.JsonObject;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.JsonCollectionTypeHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.JsonObjectTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MyBatis 插件，自动为标记了 @JsonCollection 或 @JsonObject 注解的字段
 * 注入相应的 TypeHandler，实现自动的 JSON 序列化和反序列化。
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class JsonFieldInterceptor implements Interceptor{

    private static final Logger logger = LoggerFactory.getLogger(JsonFieldInterceptor.class);

    private final ObjectMapper objectMapper;

    // 用于标记已经处理过的 MappedStatement ID，避免重复处理
    private final Set<String> processedMappedStatements = ConcurrentHashMap.newKeySet();

    public JsonFieldInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            // 获取方法参数
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];

            // 检查是否已经处理过这个 MappedStatement，避免无限循环
            if (!processedMappedStatements.contains(ms.getId())) {
                // 判断是查询还是更新操作
                String methodName = invocation.getMethod().getName();
                if ("query".equals(methodName)) {
                    processResultMap(ms);
                } else if ("update".equals(methodName)) {
                    processParameterMap(ms);
                }
            }

            return invocation.proceed();
        } catch (Exception e) {
            logger.warn("处理JSON字段时发生异常，使用默认处理: {}", e.getMessage());
            return invocation.proceed();
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

    /**
     * 处理 ResultMap，为需要 JSON 反序列化的字段设置 TypeHandler。
     */
    private void processResultMap(MappedStatement ms) {
        try {
            // 标记为已处理
            if (!processedMappedStatements.add(ms.getId())) {
                return; // 已经处理过，直接返回
            }

            if (ms.getResultMaps() == null || ms.getResultMaps().isEmpty()) {
                return;
            }

            Configuration configuration = ms.getConfiguration();
            boolean modified = false;
            List<ResultMap> newResultMaps = new ArrayList<>();

            // 遍历所有的 ResultMap
            for (ResultMap resultMap : ms.getResultMaps()) {
                List<ResultMapping> newResultMappings = new ArrayList<>();

                for (ResultMapping rm : resultMap.getResultMappings()) {
                    ResultMapping newRm = rm;
                    String property = rm.getProperty();
                    Class<?> resultType = resultMap.getType();

                    try {
                        Field field = resultType.getDeclaredField(property);
                        if (field.isAnnotationPresent(JsonCollection.class)) {
                            Type fieldType = field.getGenericType();
                            if (fieldType instanceof ParameterizedType) {
                                ParameterizedType paramType = (ParameterizedType) fieldType;
                                // 使用 TypeReference 来处理泛型
                                TypeReference<?> typeReference = createTypeReference(fieldType, resultType);
                                // 修复类型转换问题 - 强制转换 TypeReference 类型
                                @SuppressWarnings("unchecked")
                                TypeHandler<?> typeHandler = new JsonCollectionTypeHandler(
                                        (Class<?>) paramType.getActualTypeArguments()[0],
                                        objectMapper,
                                        (TypeReference<Collection<?>>) typeReference
                                );
                                newRm = createNewResultMapping(rm, configuration, typeHandler);
                                modified = true;
                            }
                        } else if (field.isAnnotationPresent(JsonObject.class)) {
                            Class<?> fieldClass = field.getType();
                            TypeHandler<?> typeHandler = new JsonObjectTypeHandler<>(fieldClass, objectMapper);
                            newRm = createNewResultMapping(rm, configuration, typeHandler);
                            modified = true;
                        }
                    } catch (NoSuchFieldException e) {
                        // 字段不存在，跳过
                    } catch (Exception e) {
                        logger.warn("处理ResultMap字段 {} 时发生异常: {}", property, e.getMessage());
                    }

                    newResultMappings.add(newRm);
                }

                if (modified) {
                    // 为修改过的 ResultMap 创建新的实例
                    ResultMap newResultMap = new ResultMap.Builder(
                            configuration,
                            resultMap.getId(),
                            resultMap.getType(),
                            newResultMappings
                    ).build();
                    newResultMaps.add(newResultMap);
                } else {
                    // 没有修改则保留原 ResultMap
                    newResultMaps.add(resultMap);
                }
            }

            // 只有在有修改的情况下才替换 MappedStatement
            if (modified) {
                MappedStatement newMs = copyMappedStatement(ms, newResultMaps, ms.getParameterMap());
                replaceMappedStatement(ms, newMs);
            }
        } catch (Exception e) {
            logger.warn("处理ResultMap时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 处理 ParameterMap，为需要 JSON 序列化的字段设置 TypeHandler。
     */
    private void processParameterMap(MappedStatement ms) {
        try {
            // 标记为已处理
            if (!processedMappedStatements.add(ms.getId())) {
                return; // 已经处理过，直接返回
            }

            ParameterMap parameterMap = ms.getParameterMap();
            if (parameterMap == null) {
                return;
            }

            Configuration configuration = ms.getConfiguration();

            boolean modified = false;
            List<ParameterMapping> newParameterMappings = new ArrayList<>();

            for (ParameterMapping pm : parameterMap.getParameterMappings()) {
                ParameterMapping newPm = pm;
                String property = pm.getProperty();
                Class<?> parameterType = ms.getParameterMap().getType();

                try {
                    Field field = parameterType.getDeclaredField(property);
                    if (field.isAnnotationPresent(JsonCollection.class)) {
                        Type fieldType = field.getGenericType();
                        if (fieldType instanceof ParameterizedType) {
                            ParameterizedType paramType = (ParameterizedType) fieldType;
                            // 使用 TypeReference 来处理泛型
                            TypeReference<?> typeReference = createTypeReference(fieldType, parameterType);
                            // 修复类型转换问题 - 强制转换 TypeReference 类型
                            @SuppressWarnings("unchecked")
                            TypeHandler<?> typeHandler = new JsonCollectionTypeHandler(
                                    (Class<?>) paramType.getActualTypeArguments()[0],
                                    objectMapper,
                                    (TypeReference<Collection<?>>) typeReference
                            );
                            newPm = createNewParameterMapping(pm, configuration, typeHandler);
                            modified = true;
                        }
                    } else if (field.isAnnotationPresent(JsonObject.class)) {
                        Class<?> fieldClass = field.getType();
                        TypeHandler<?> typeHandler = new JsonObjectTypeHandler<>(fieldClass, objectMapper);
                        newPm = createNewParameterMapping(pm, configuration, typeHandler);
                        modified = true;
                    }
                } catch (NoSuchFieldException e) {
                    // 字段不存在，跳过
                } catch (Exception e) {
                    logger.warn("处理ParameterMap字段 {} 时发生异常: {}", property, e.getMessage());
                }

                newParameterMappings.add(newPm);
            }

            if (modified) {
                ParameterMap newParameterMap = new ParameterMap.Builder(
                        configuration,
                        parameterMap.getId(),
                        parameterMap.getType(),
                        newParameterMappings
                ).build();
                // 替换 MappedStatement 中的 ParameterMap
                MappedStatement newMs = copyMappedStatement(ms, ms.getResultMaps(), newParameterMap);
                replaceMappedStatement(ms, newMs);
            }
        } catch (Exception e) {
            logger.warn("处理ParameterMap时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 创建新的 ResultMapping，包含指定的 TypeHandler。
     */
    private ResultMapping createNewResultMapping(ResultMapping original, Configuration configuration, TypeHandler<?> typeHandler) {
        ResultMapping.Builder builder = new ResultMapping.Builder(
                configuration,
                original.getProperty(),
                original.getColumn(),
                original.getJavaType()
        );
        builder.jdbcType(original.getJdbcType());
        builder.typeHandler(typeHandler);
        builder.flags(original.getFlags());
        builder.columnPrefix(original.getColumnPrefix());
        builder.notNullColumns(original.getNotNullColumns());
        builder.nestedResultMapId(original.getNestedResultMapId());
        builder.foreignColumn(original.getForeignColumn());
        builder.lazy(original.isLazy());
        return builder.build();
    }

    /**
     * 创建新的 ParameterMapping，包含指定的 TypeHandler。
     */
    private ParameterMapping createNewParameterMapping(ParameterMapping original, Configuration configuration, TypeHandler<?> typeHandler) {
        ParameterMapping.Builder builder = new ParameterMapping.Builder(
                configuration,
                original.getProperty(),
                original.getJavaType()
        );
        builder.jdbcType(original.getJdbcType());
        builder.resultMapId(original.getResultMapId());
        builder.mode(original.getMode());
        builder.numericScale(original.getNumericScale());
        builder.typeHandler(typeHandler);
        builder.jdbcTypeName(original.getJdbcTypeName());
        builder.expression(original.getExpression());
        return builder.build();
    }

    /**
     * 创建 MappedStatement 的副本，替换 ResultMap 或 ParameterMap。
     */
    private MappedStatement copyMappedStatement(MappedStatement original, List<ResultMap> resultMaps, ParameterMap parameterMap) {
        MappedStatement.Builder builder = new MappedStatement.Builder(
                original.getConfiguration(),
                original.getId(),
                original.getSqlSource(),
                original.getSqlCommandType()
        );
        builder.resource(original.getResource());
        builder.fetchSize(original.getFetchSize());
        builder.timeout(original.getTimeout());
        builder.statementType(original.getStatementType());
        builder.keyGenerator(original.getKeyGenerator());
        String[] keyProperties = original.getKeyProperties();
        builder.keyProperty(keyProperties == null ? null : String.join(",", keyProperties));
        String[] keyColumns = original.getKeyColumns();
        builder.keyColumn(keyColumns == null ? null : String.join(",", keyColumns));
        builder.databaseId(original.getDatabaseId());
        builder.lang(original.getLang());
        builder.resultSetType(original.getResultSetType());
        builder.flushCacheRequired(original.isFlushCacheRequired());
        builder.useCache(original.isUseCache());
        builder.resultOrdered(original.isResultOrdered());
        builder.parameterMap(parameterMap);
        builder.resultMaps(resultMaps);
        builder.cache(original.getCache());
        return builder.build();
    }

    /**
     * 使用反射替换 Configuration 中的 MappedStatement。
     * 这是一个关键且有风险的操作，因为它修改了 MyBatis 的内部状态。
     */
    private void replaceMappedStatement(MappedStatement oldMs, MappedStatement newMs) {
        Configuration configuration = oldMs.getConfiguration();

        try {
            // 尝试使用反射方式（更精确控制）
            Field field = Configuration.class.getDeclaredField("mappedStatements");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, MappedStatement> statementMap = (Map<String, MappedStatement>) field.get(configuration);

            synchronized (configuration) {
                // 先移除旧的再添加新的，确保原子性
                statementMap.remove(oldMs.getId());
                statementMap.put(newMs.getId(), newMs);
            }
        } catch (Exception e) {
            // 如果反射失败，回退到使用 MyBatis API
            try {
                // 先尝试移除旧的（如果 API 支持）
                configuration.addMappedStatement(newMs);
            } catch (Exception fallbackException) {
                logger.warn("Failed to replace MappedStatement: {}", fallbackException.getMessage());
            }
        }
    }

    /**
     * 根据字段的泛型类型创建 TypeReference。
     * 这对于正确反序列化泛型集合至关重要。
     */
    @SuppressWarnings("unchecked")
    private TypeReference<?> createTypeReference(Type fieldType, Class<?> ownerClass) {
        return new TypeReference<Collection<Object>>() {
            @Override
            public Type getType() {
                return fieldType;
            }
        };
    }
}
