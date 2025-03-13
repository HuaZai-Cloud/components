package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.ArrayUtils;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.*;

public class BeanUtils {

    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    public static void copyProperties(@NonNull Object source, @NonNull Object target, String... ignoreProperties) {

        List<String> ignoreList = ArrayUtils.isNotEmpty(ignoreProperties) ? new ArrayList<>(Arrays.asList(ignoreProperties)) : CollectionUtils.emptyList();
        if (!ignoreList.contains(SERIAL_VERSION_UID)) {
            ignoreList.add(SERIAL_VERSION_UID);
        }
        copyProperties(source, source.getClass(), target, target.getClass(),ignoreList);
    }

    private static void copyProperties(@NonNull Object source, Class<?> sourceClazz, @NonNull Object target, Class<?> targetClazz, List<String> ignoreList) {
        Field[] sourceFields = sourceClazz.getDeclaredFields();
        Map<String, Field> targetFieldMap = getFieldMap(targetClazz);
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            if (ignoreList.contains(fieldName)) {
                continue;
            }
            Field targetField = targetFieldMap.get(fieldName);
            if (targetField == null) {
                continue;
            }
            try {
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                Object value = sourceField.get(source);
                targetField.set(target, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to copy property: " + fieldName, e);
            }
        }


    }

    private static Map<String, Field> getFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }
}
