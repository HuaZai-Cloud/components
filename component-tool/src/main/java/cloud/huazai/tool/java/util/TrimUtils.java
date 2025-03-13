package cloud.huazai.tool.java.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class TrimUtils {


    /**
     * 对象的指定属性去除前后空格
     *
     * @param object     目标对象
     * @param fieldNames 需要去除空格的属性名数组
     * @param <T>        对象的类型
     */
    public static <T> void trimFields(T object, String... fieldNames) {

        if (object == null) {
            return;
        }
        Class<?> clazz = object.getClass();

        if (fieldNames.length < 1) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                processField(object, field);
            }
        } else {
            for (String fieldName : fieldNames) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    processField(object, field);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    private static <T> void processField(T object, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value instanceof String) {
                String trimmedValue = ((String) value).trim();
                field.set(object, trimmedValue);
            } else if (value instanceof Collection) {
                for (Object item : (Collection<?>) value) {
                    trimFields(item);
                }
            } else if (value != null) {
                trimFields(value);
            }
        } catch (IllegalAccessException e) {
            // 处理无法访问字段的情况
            e.printStackTrace();
        }
    }
}
