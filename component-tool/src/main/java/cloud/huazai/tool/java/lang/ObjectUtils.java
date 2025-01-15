package cloud.huazai.tool.java.lang;

import lombok.NonNull;

/**
 * ObjectUtils
 *
 * @author devon
 * @since 2025/1/15
 */

public class ObjectUtils {

    private static final String defaultMessage = "Object Is Null";


    public static <T> void requireNonNull(T obj, String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        StringUtils.requireNonBlank(message, "message");
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }


}
