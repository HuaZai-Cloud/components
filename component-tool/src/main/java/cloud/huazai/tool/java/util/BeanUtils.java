package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.ArrayUtils;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BeanUtils {

    public static void copyProperties(@NonNull Object source, @NonNull Object target, String... ignoreProperties) {
        copyProperties(source, source.getClass(), target, target.getClass(), ArrayUtils.isNotEmpty(ignoreProperties) ? Arrays.asList(ignoreProperties) : CollectionUtils.emptyList());
    }

    private static void copyProperties(@NonNull Object source, Class<?> sourceClazz, @NonNull Object target, Class<?> targetClazz, List<String> ignorePropertieList) {

        Field[] sourceFields = sourceClazz.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            if (ignorePropertieList.contains(fieldName)) {
                continue;
            }
            try {
                Field targetField = targetClazz.getDeclaredField(fieldName);
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object value = sourceField.get(source);
                targetField.set(target, value);

            } catch (NoSuchFieldException ignored) {
                // 跳过当前字段
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to copy property: " + fieldName, e);
            }
        }


    }
}
