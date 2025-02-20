package cloud.huazai.tool.java.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class TrimUtils {

    /**
     * 对集合中的对象指定属性去除前后空格
     *
     * @param collection 集合对象
     * @param fieldNames 需要去除空格的属性名数组
     * @param <T>        集合中对象的类型
     */
    public static <T> void trimFields(Collection<T> collection, String... fieldNames) {
        if (CollectionUtils.isEmpty(collection) || fieldNames.length < 1) {
            return;
        }

        for (T item : collection) {
            if (item == null) {
                continue;
            }

            for (String fieldName : fieldNames) {
                try {
                    Field field = item.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true); // 允许访问私有字段

                    Object value = field.get(item);
                    if (value instanceof String) {
                        String trimmedValue = ((String) value).trim();
                        field.set(item, trimmedValue);
                    }
                } catch (Exception e) {
                    // 处理异常，例如字段不存在或无法访问
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对象的指定属性去除前后空格
     *
     * @param object     目标对象
     * @param fieldNames 需要去除空格的属性名数组
     * @param <T>        对象的类型
     */
    public static <T> void trimFields(T object, String... fieldNames) {
        if (object == null || fieldNames.length < 1) {
            return;
        }

        for (String fieldName : fieldNames) {
            try {
                // 获取对象的类
                Class<?> clazz = object.getClass();
                // 获取指定字段
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true); // 允许访问私有字段

                // 获取字段的值
                Object value = field.get(object);
                if (value instanceof String) {
                    // 去除前后空格
                    String trimmedValue = ((String) value).trim();
                    // 设置新的值
                    field.set(object, trimmedValue);
                } else if (value instanceof Collection) {
                    // 如果字段是集合，递归处理每个元素
                    for (Object item : (Collection<?>) value) {
                        trimFields(item, fieldNames);
                    }
                } else if (value != null) {
                    // 如果字段是对象，递归处理
                    trimFields(value, fieldNames);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 处理异常，例如字段不存在或无法访问
                e.printStackTrace();
            }
        }
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
}
