package cloud.huazai.tool.java.util;


import cloud.huazai.tool.java.lang.ObjectUtils;
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


    public static <T> Collection<T> immutableEmptyCollection() {

        return Collections.emptyList();
    }

    public static <T> List<T> immutableEmptyList() {
        return Collections.emptyList();
    }

    public static <T> Set<T> immutableEmptySet() {
        return Collections.emptySet();
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

    public static boolean isCollection(Object obj) {
        return ObjectUtils.isNotNull(obj) && obj instanceof Collection;
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

    public static <T> Collection<Collection<T>> partition(Collection<T> collection, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        Collection<Collection<T>> result = new ArrayList<>((collection.size() + size - 1) / size);
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Collection<T> chunk = new ArrayList<>(Math.min(size, collection.size()));
            for (int i = 0; i < size && iterator.hasNext(); i++) {
                chunk.add(iterator.next());
            }
            result.add(chunk);
        }
        return result;
    }

    public static <T> String toJsonString(Collection<T> collection) {
        return JsonUtils.toJsonString(collection);
    }



}
