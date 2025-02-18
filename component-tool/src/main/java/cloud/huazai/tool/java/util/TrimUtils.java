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
        if (collection == null || fieldNames == null || fieldNames.length == 0) {
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
}
