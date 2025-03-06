package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.ArrayUtils;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanUtils {

    public static void copyProperties(@NonNull Object source, @NonNull Object target, String... ignoreProperties)  {
        copy(source, target, ignoreProperties);
    }

    private static void copy(@NonNull Object source, @NonNull Object target, String... ignoreProperties)  {

        List<String> ignoreList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(ignoreProperties)) {
             ignoreList = Arrays.asList(ignoreProperties);
        }
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            if (ignoreList.contains(fieldName)) {
                continue;
            }
            try {
                Field targetField = targetClass.getDeclaredField(fieldName);
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
