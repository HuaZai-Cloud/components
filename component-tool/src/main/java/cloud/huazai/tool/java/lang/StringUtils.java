package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.constant.StringConstant;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StringUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class StringUtils {

    private static final String defaultMessage = "Object Is Blank";

    public static boolean isBlank(CharSequence cs) {
        int strLength = length(cs);
        if (strLength != 0) {
            for (int i = 0; i < strLength; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static void requireNonBlank(CharSequence cs,String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        if (isBlank(cs)) {
            throw new NullPointerException(message);
        }
    }

    public static List<String> split(@NonNull String str, String separator){
        if (isBlank(separator)) {
            separator = StringConstant.COMMA;
        }
        return Arrays.stream(str.split(separator)).collect(Collectors.toList());
    }

    public static <T> String join(@NonNull Collection<T> collection, String separator) {
        if (isBlank(separator)) {
            separator = StringConstant.COMMA;
        }
        return collection.stream()               // 将 Collection 转换为 Stream
                .map(String::valueOf)            // 将每个元素转换为 String 类型
                .collect(Collectors.joining(separator));  // 使用指定分隔符连接
    }

}
