package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.util.CollectionUtils;
import cloud.huazai.tool.java.util.JsonUtils;
import cloud.huazai.tool.java.util.MapUtils;
import lombok.NonNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * ObjectUtils
 *
 * @author devon
 * @since 2025/1/15
 */

public class ObjectUtils {

    private static final String defaultMessage = "Object Is Null";


    public static <T> void requireNonNull(T obj, String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        StringUtils.requireNonBlank(message, "message");
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    public static String toString(@NonNull Object obj) {

        if (obj instanceof String) {
            return (String) obj;
        } else if (ArrayUtils.isArray(obj)) {
            return ArrayUtils.toString(obj);
        } else if (CollectionUtils.isCollection(obj)) {
            return CollectionUtils.toJsonString((Collection<?>) obj); // 转为 JSON 格式
        } else if (MapUtils.isMap(obj)) {
            return JsonUtils.toJsonString((Map<?, ?>) obj); // 转为 JSON 格式
        }
        return obj.toString();
    }


    public static boolean isNull(Object obj) {
        return obj == null;
    }
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 对象空判断
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Iterable) {
            return !((Iterable<?>) obj).iterator().hasNext();
        }
        if (obj instanceof Iterator) {
            return !((Iterator<?>) obj).hasNext();
        }
        // else
        return false;
    }
}
