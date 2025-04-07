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

    /**
     * 交集
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <T> 对象类型
     * @return 交集集合
     *
     */
    public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2) {
        List<T> result = new ArrayList<>(coll1);
        result.retainAll(coll2); // 保留两个集合都有的元素
        return result;
    }

    /**
     * 并集
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <T> 对象类型
     * @return 并集集合
     */
    public static <T> Collection<T> union(Collection<T> coll1, Collection<T> coll2) {
        Set<T> set = new HashSet<>(coll1);
        set.addAll(coll2); // 添加所有元素到集合中（自动去重）
        return new ArrayList<>(set);
    }


    /**
     * 差集
     * @param coll1
     * @param coll2
     * @param <T>
     * @return
     */
    public static <T> Collection<T> difference(Collection<T> coll1, Collection<T> coll2) {
        List<T> result = new ArrayList<>(coll1);
        result.removeAll(coll2); // 移除 collection1 中存在于 collection2 的元素
        return result;
    }


    /**
     * 补集
     * @param universalColl 全集
     * @param coll 子集
     * @param <T> 对象类型
     * @return 补集集合
     */
    public static <T> Collection<T> complement(Collection<T> universalColl, Collection<T> coll) {
        List<T> result = new ArrayList<>(universalColl);
        result.removeAll(coll); // 移除子集中的元素
        return result;
    }

    /**
     * 对称差集
     * @param collection1
     * @param collection2
     * @return
     * @param <T>
     */
    public static <T> Collection<T> symmetricDifference(Collection<T> collection1, Collection<T> collection2) {
        Collection<T> union = union(collection1, collection2); // 并集
        Collection<T> intersection = intersection(collection1, collection2); // 交集
        union.removeAll(intersection); // 移除交集部分
        return union;
    }



}
