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
        }
        if (isArray(obj)) {
            return toArrayString(obj);
        }
        return obj.toString();

    }

    private static String toArrayString(Object obj) {
        if (obj instanceof long[]) {
            return Arrays.toString(((long[]) obj));
        } else if (obj instanceof int[]) {
            return Arrays.toString(((int[]) obj));
        } else if (obj instanceof short[]) {
            return Arrays.toString(((short[]) obj));
        } else if (obj instanceof char[]) {
            return Arrays.toString(((char[]) obj));
        } else if (obj instanceof byte[]) {
            return Arrays.toString(((byte[]) obj));
        } else if (obj instanceof boolean[]) {
            return Arrays.toString(((boolean[]) obj));
        } else if (obj instanceof float[]) {
            return Arrays.toString(((float[]) obj));
        } else if (obj instanceof double[]) {
            return Arrays.toString(((double[]) obj));
        } else  {
            return Arrays.deepToString(((Object[]) obj));
        }
    }

    public static int length(Object array) {
        return null == array ? 0 : Array.getLength(array);
    }

}
