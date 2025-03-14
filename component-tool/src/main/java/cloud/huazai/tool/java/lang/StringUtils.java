package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.constant.StringConstant;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * StringUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class StringUtils {

    public static final String BLANK = StringConstant.BLANK;

    public static final String NULL = StringConstant.NULL;

    private static final String defaultMessage = "Object Is Blank";

    public static boolean isString(Object obj){
        return obj instanceof String;
    }

    public static boolean isBlank(CharSequence str) {
        int strLength = length(str);
        if (strLength != 0) {
            for (int i = 0; i < strLength; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static void requireNonBlank(CharSequence str,String message) {
        if (StringUtils.isBlank(message)) {
            message = defaultMessage;
        }
        if (isBlank(str)) {
            throw new NullPointerException(message);
        }
    }

    public static List<String> split(@NonNull String str, String separator){
        if (isBlank(separator)) {
            separator = StringConstant.COMMA;
        }
        if (separator.isEmpty()) {
            return Arrays.asList(str.split("")); // 按字符分割
        }
        return Arrays.stream(str.split(Pattern.quote(separator))).collect(Collectors.toList());
    }

    public static <T> String join(@NonNull Collection<T> collection, String separator) {
        if (isBlank(separator)) {
            separator = StringConstant.COMMA;
        }
        return collection.stream()               // 将 Collection 转换为 Stream
                .map(String::valueOf)            // 将每个元素转换为 String 类型
                .collect(Collectors.joining(separator));  // 使用指定分隔符连接
    }


    public static String format(@NonNull CharSequence str, Object... params) {
        if (isNotBlank(str) && ArrayUtils.isNotEmpty(params)) {
            String strString = str.toString();
            StringBuilder result = new StringBuilder(strString.length() + params.length * 10);
            int handledPosition = 0;
            int paramIndex = 0;

            while (paramIndex < params.length) {
                int delimIndex = strString.indexOf("{}", handledPosition);
                if (delimIndex == -1) {
                    break;
                }
                result.append(strString, handledPosition, delimIndex)
                        .append(toString(params[paramIndex++]));
                handledPosition = delimIndex + 2;
            }
            result.append(strString, handledPosition, strString.length());
            return result.toString();
        }
        return str.toString();
    }



    public static String toString(Object obj) {

        if (obj == null) {
            return null;
        }
        return ObjectUtils.toString(obj);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {

        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        for (int i = 0; i < cs1.length(); ++i) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }



}
