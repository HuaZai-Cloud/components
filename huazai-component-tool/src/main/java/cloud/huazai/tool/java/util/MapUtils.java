package cloud.huazai.tool.java.util;

import java.util.Collection;
import java.util.Map;

/**
 * MapUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class MapUtils {

    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?,?> map) {

        System.out.println("map = " + map);

        System.out.println("map = " + map);
        System.out.println("map = " + map);

        return !isEmpty(map);
    }
}
