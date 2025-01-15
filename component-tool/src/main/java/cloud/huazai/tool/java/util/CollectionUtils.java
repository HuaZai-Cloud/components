package cloud.huazai.tool.java.util;


import cloud.huazai.tool.java.lang.StringUtils;

import java.util.*;

/**
 * CollectionUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class CollectionUtils {

    private static final String defaultMessage = "Collection Is Empty";

    public static Collection EMPTY_COLLECTION = new ArrayList<>();
    public static List EMPTY_LIST = new ArrayList<>();
    public static Set EMPTY_SET = new HashSet<>();

    public static <T> Collection<T> immutableEmptyCollection() {
        return List.of();
    }

    public static <T> List<T> immutableEmptyList() {
        return List.of();
    }

    public static <T> Set<T> immutableEmptySet() {
        return Set.of();
    }

    public static <T> Collection<T> emptyCollection() {
        return new ArrayList<>();
    }

    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static void requireNonEmpty(Collection<?> coll, String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        if (isEmpty(coll)) {
            throw new NullPointerException(message);
        }
    }




}
