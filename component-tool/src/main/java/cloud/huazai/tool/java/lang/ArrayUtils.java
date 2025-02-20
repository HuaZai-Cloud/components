package cloud.huazai.tool.java.lang;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtils {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        } else if (obj instanceof long[]) {
            return Arrays.toString((long[])((long[])obj));
        } else if (obj instanceof int[]) {
            return Arrays.toString((int[])((int[])obj));
        } else if (obj instanceof short[]) {
            return Arrays.toString((short[])((short[])obj));
        } else if (obj instanceof char[]) {
            return Arrays.toString((char[])((char[])obj));
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[])((byte[])obj));
        } else if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[])((boolean[])obj));
        } else if (obj instanceof float[]) {
            return Arrays.toString((float[])((float[])obj));
        } else if (obj instanceof double[]) {
            return Arrays.toString((double[])((double[])obj));
        } else {
            if (isArray(obj)) {
                try {
                    return Arrays.deepToString((Object[])((Object[])obj));
                } catch (Exception var2) {
                }
            }

            return obj.toString();
        }
    }

    public static int length(Object array) throws IllegalArgumentException {
        return null == array ? 0 : Array.getLength(array);
    }

}
