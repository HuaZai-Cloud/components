package cloud.huazai.dataaccesslayer.mybatis.core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * JsonCollectionTypeHandler
 *
 * @author Devon
 * @since 2025/8/1 17:42
 */

public class JsonCollectionTypeHandler<T extends Collection<?>> extends BaseTypeHandler<T> {

    @Getter
    private final Class<?> elementClass;
    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    public JsonCollectionTypeHandler(Class<?> elementClass, ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this.elementClass = elementClass;
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error serializing collection to JSON", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private T parseJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            // 使用 TypeReference 确保泛型信息正确
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to collection", e);
        }
    }

}
