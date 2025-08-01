package cloud.huazai.dataaccesslayer.mybatis.core.interceptor;

import cloud.huazai.dataaccesslayer.mybatis.annotation.JsonCollection;
import cloud.huazai.dataaccesslayer.mybatis.annotation.JsonObject;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.JsonCollectionTypeHandler;
import cloud.huazai.dataaccesslayer.mybatis.core.handler.JsonObjectTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * MyBatis-Plus InnerInterceptor 插件，自动为标记了 @JsonCollection 或 @JsonObject 注解的字段
 * 注入相应的 TypeHandler，实现自动的 JSON 序列化和反序列化。
 */
@Component // 确保 Spring 能管理这个 Bean
public class JsonCollectionPlugin implements InnerInterceptor {
    private final ObjectMapper objectMapper;

    // 使用构造函数注入 ObjectMapper
    @Autowired
    public JsonCollectionPlugin(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 在查询前拦截，修改 ResultMap 以注入 TypeHandler。
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        processResultMap(ms);
    }

    /**
     * 在更新（INSERT/UPDATE）前拦截，修改 MappedStatement 的 ParameterMap 以注入 TypeHandler。
     */
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        processParameterMap(ms);
    }

    /**
     * 处理 ResultMap，为需要 JSON 反序列化的字段设置 TypeHandler。
     */
    private void processResultMap(MappedStatement ms) {
        ResultMap resultMap = ms.getResultMaps().get(0); // 简化处理，假设只有一个 ResultMap
        Configuration configuration = ms.getConfiguration();

        boolean modified = false;
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
                        Class<?> elementClass = (Class<?>) paramType.getActualTypeArguments()[0];
                        // 使用 TypeReference 来处理泛型
                        TypeReference<?> typeReference = createTypeReference(fieldType, resultType);
                        TypeHandler<?> typeHandler = new JsonCollectionTypeHandler<>((Class<?>) paramType.getActualTypeArguments()[0], objectMapper, typeReference);
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
            }

            newResultMappings.add(newRm);
        }

        if (modified) {
            ResultMap newResultMap = new ResultMap.Builder(
                    configuration,
                    resultMap.getId(),
                    resultMap.getType(),
                    newResultMappings,
                    resultMap.getDiscriminator()
            ).build();
            // 替换 MappedStatement 中的 ResultMap
            replaceResultMap(ms, newResultMap);
        }
    }

    /**
     * 处理 ParameterMap，为需要 JSON 序列化的字段设置 TypeHandler。
     */
    private void processParameterMap(MappedStatement ms) {
        ParameterMap parameterMap = ms.getParameterMap();
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
                        Class<?> elementClass = (Class<?>) paramType.getActualTypeArguments()[0];
                        // 使用 TypeReference 来处理泛型
                        TypeReference<?> typeReference = createTypeReference(fieldType, parameterType);
                        TypeHandler<?> typeHandler = new JsonCollectionTypeHandler<>((Class<?>) paramType.getActualTypeArguments()[0], objectMapper, typeReference);
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
            replaceParameterMap(ms, newParameterMap);
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
        builder.columnPrefix(original.getColumnPrefix());
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
     * 安全地替换 MappedStatement 中的 ResultMap。
     * 注意：MappedStatement 是不可变的，所以需要创建一个新的实例。
     */
    private void replaceResultMap(MappedStatement ms, ResultMap newResultMap) {
        MappedStatement newMs = copyMappedStatement(ms, Collections.singletonList(newResultMap), ms.getParameterMap());
        replaceMappedStatement(ms, newMs);
    }

    /**
     * 安全地替换 MappedStatement 中的 ParameterMap。
     * 注意：MappedStatement 是不可变的，所以需要创建一个新的实例。
     */
    private void replaceParameterMap(MappedStatement ms, ParameterMap newParameterMap) {
        MappedStatement newMs = copyMappedStatement(ms, ms.getResultMaps(), newParameterMap);
        replaceMappedStatement(ms, newMs);
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
        builder.keyProperty(keyProperties == null ? null : Arrays.toString(Arrays.copyOf(keyProperties, keyProperties.length)));
        String[] keyColumns = original.getKeyColumns();
        builder.keyColumn(keyColumns == null ? null : Arrays.toString(Arrays.copyOf(keyColumns, keyColumns.length)));
        builder.databaseId(original.getDatabaseId());
        builder.lang(original.getLang());
        builder.resultSetType(original.getResultSetType());
        builder.flushCacheRequired(original.isFlushCacheRequired());
        builder.useCache(original.isUseCache());
        builder.resultOrdered(original.isResultOrdered());
        builder.parameterMap(parameterMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(original.getResultSetType());
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
            // 获取 mappedStatements 字段
            Field field = Configuration.class.getDeclaredField("mappedStatements");
            field.setAccessible(true);

            // 获取当前的 mappedStatements Map
            Map<String, MappedStatement> statementMap = (Map<String, MappedStatement>) field.get(configuration);

            synchronized (configuration) {
                // 移除旧的 MappedStatement
                statementMap.remove(oldMs.getId());
                // 添加新的 MappedStatement
                statementMap.put(newMs.getId(), newMs);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to replace MappedStatement via reflection", e);
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
