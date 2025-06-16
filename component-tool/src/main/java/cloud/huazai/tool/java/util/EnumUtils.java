package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * EnumUtils
 *
 * @author devon
 * @since 2025-06-13 10:29
 */
public class EnumUtils {


    public static boolean isEnum(@NonNull Class<?> clazz) {
        return clazz.isEnum();
    }

    public static boolean isEnum(@NonNull Object obj) {
        return obj.getClass().isEnum();
    }

    public static boolean isNotEnum(@NonNull Class<?> clazz) {
        return !isEnum(clazz);
    }

    public static boolean isNotEnum(@NonNull Object obj) {
        return !isEnum(obj);
    }

    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        return ArrayUtils.isEmpty(enumConstants) ?  CollectionUtils.emptyList() : Arrays.asList(enumConstants);
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, int index) {
        E[] enumConstants = enumClass.getEnumConstants();
        return (E)(index >= 0 && index < enumConstants.length ? enumConstants[index] : null);
    }

    public static <E extends Enum<E>> E getEnum(@NonNull Class<E> enumClass, String name) {
      return   Enum.valueOf(enumClass, name);
    }

    public static <E extends Enum<E>> E getEnumOrDefault(@NonNull Class<E> enumClass, String name, E defaultValue) {

        if (StringUtils.isBlank(name)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return defaultValue;
        }

    }

    public static <E extends Enum<E>> E getEnumOrDefaultNull(@NonNull Class<E> enumClass, String name) {
        return getEnumOrDefault(enumClass, name, null);
    }



}
