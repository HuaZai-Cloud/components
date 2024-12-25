package cloud.huazai.tool.java.util;


import java.util.Collection;

/**
 * CollectionUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }




}
