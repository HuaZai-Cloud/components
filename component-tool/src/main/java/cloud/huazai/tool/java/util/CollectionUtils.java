package cloud.huazai.tool.java.util;


import cloud.huazai.tool.java.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CollectionUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class CollectionUtils {

    private static final String defaultMessage = "Collection Is Empty";


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

    public static <T> Collection<Collection<T>> partition(Collection<T> collection, int size) {

        Collection<Collection<T>> result = new ArrayList<>((collection.size() + size - 1) / size);
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Collection<T> chunk = new ArrayList<>(size);
            for (int i = 0; i < size && iterator.hasNext(); i++) {
                chunk.add(iterator.next());
            }
            result.add(chunk);
        }
        return result;
    }





}
