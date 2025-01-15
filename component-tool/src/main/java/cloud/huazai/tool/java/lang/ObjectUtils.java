package cloud.huazai.tool.java.lang;

import lombok.NonNull;

/**
 * ObjectUtils
 *
 * @author devon
 * @since 2025/1/15
 */

public class ObjectUtils {

    public static <T> void requireNonNull(T obj,String message) {
        StringUtils.requireNonBlank(message,"message");
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}
