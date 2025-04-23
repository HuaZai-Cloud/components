package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MapUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class MapUtils {

    private static final String defaultMessage = "Collection Is Empty";

    public static <K,V> Map<K,V> immutableEmptyMap() {
        return Collections.emptyMap();
    }

    public static <K,V> Map<K,V> emptyMap() {
        return new HashMap<>();
    }

    public static <K,V> boolean isEmpty(Map<K,V> map) {
        return map == null || map.isEmpty();
    }

    public static <K,V> boolean isNotEmpty(Map<K,V> map) {
        return !isEmpty(map);
    }

    public static boolean isMap(Object obj) {
        return ObjectUtils.isNotNull(obj) && obj instanceof Map;
    }

    public static <K,V> void requireNonEmpty(Map<K,V> map, String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        if (isEmpty(map)) {
            throw new NullPointerException(message);
        }
    }
}
