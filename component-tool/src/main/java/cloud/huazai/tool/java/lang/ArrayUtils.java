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
        if (obj.getClass().getComponentType().isPrimitive()) {
            return Arrays.toString((Object[]) obj);
        }
        return Arrays.deepToString((Object[]) obj);
    }

    public static int length(Object array) {
        return null == array ? 0 : Array.getLength(array);
    }

    public static int indexOf(char[] array, char value) {
        if (null != array) {
            for(int i = 0; i < array.length; ++i) {
                if (value == array[i]) {
                    return i;
                }
            }
        }

        return -1;
    }

}
