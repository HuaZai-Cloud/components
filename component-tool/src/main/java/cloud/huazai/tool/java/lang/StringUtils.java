package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.constant.CharConstant;
import cloud.huazai.tool.java.constant.StringConstant;
import lombok.NonNull;

import java.util.ArrayList;
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

    public static String substring(CharSequence str, int start) {
        if (isEmpty(str)) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return StringConstant.BLANK;
        }
        return str.toString().substring(start);
    }

    public static String substring(CharSequence str, int start, int end) {
        if (isEmpty(str)) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return StringConstant.BLANK;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.toString().substring(start, end);
    }


    public static String toUnderlineCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0))); // 第一个字符小写

        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append(CharConstant.UNDERLINE).append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static String blankToDefault(CharSequence str, String defaultStr) {
        return isBlank(str) ? defaultStr : str.toString();
    }



    public static boolean contains(CharSequence str, CharSequence searchStr) {
        return null != str && null != searchStr ? str.toString().contains(searchStr) : false;
    }

    public static List<String> splitTrim(String addresses, String separator) {
        if (StringUtils.isBlank(addresses)) {
            return new ArrayList<>();
        }

        // 1. 先分割 (Commons 的 split 会忽略空字符串，但不会 trim 空格)
        List<String> rawList = StringUtils.split(addresses, separator);

        // 2. 使用 Stream 对每个元素进行 trim
        return rawList.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())// 防止 trim 后变成空字符串
                .collect(Collectors.toList());

    }

    public static boolean startWith(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return startWith(str, prefix, ignoreCase, false);
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        if (null != str && null != prefix) {
            boolean isStartWith = str.toString().regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());
            if (!isStartWith) {
                return false;
            } else {
                return !ignoreEquals || !equals(str, prefix, ignoreCase);
            }
        } else if (ignoreEquals) {
            return false;
        } else {
            return null == str && null == prefix;
        }
    }

    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            return str2 == null;
        } else if (null == str2) {
            return false;
        } else {
            return ignoreCase ? str1.toString().equalsIgnoreCase(str2.toString()) : str1.toString().contentEquals(str2);
        }
    }


}
