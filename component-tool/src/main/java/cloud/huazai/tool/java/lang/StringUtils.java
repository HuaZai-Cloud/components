package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.constant.StringConstant;
import lombok.NonNull;

import java.nio.ByteBuffer;
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

    static final String BLANK = StringConstant.BLANK;

    private static final String defaultMessage = "Object Is Blank";

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

    public static String format(@NonNull CharSequence str, Object... params) {
        return ArrayUtils.isNotEmpty(params) && isNotBlank(str) ? format(str.toString(), params) : str.toString();
    }

    public static String format(@NonNull String str, Object... params) {
        if (isNotBlank(str) && ArrayUtils.isNotEmpty(params)) {
            int strLength = str.length();
            StringBuilder stuBui = new StringBuilder(strLength + 50);
            int handledPosition = 0;

            for(int paramIndex = 0; paramIndex < params.length; ++paramIndex) {
                int delimIndex = str.indexOf("{}", handledPosition);
                if (delimIndex == -1) {
                    if (handledPosition == 0) {
                        return str;
                    }

                    stuBui.append(str, handledPosition, strLength);
                    return stuBui.toString();
                }

                if (delimIndex > 0 && str.charAt(delimIndex - 1) == '\\') {
                    if (delimIndex > 1 && str.charAt(delimIndex - 2) == '\\') {
                        stuBui.append(str, handledPosition, delimIndex - 1);
                        stuBui.append(str(params[paramIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        --paramIndex;
                        stuBui.append(str, handledPosition, delimIndex - 1);
                        stuBui.append('{');
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    stuBui.append(str, handledPosition, delimIndex);
                    stuBui.append(str(params[paramIndex]));
                    handledPosition = delimIndex + 2;
                }
            }

            stuBui.append(str, handledPosition, str.length());
            return stuBui.toString();
        } else {
            return str;
        }
    }



    public static String str(Object obj) {
        if (null == obj) {
            return null;
        } else if (obj instanceof String) {
            return (String)obj;
        } else if (obj instanceof byte[]) {
            return str((byte[])((byte[])obj));
        } else if (obj instanceof Byte[]) {
            return str((Byte[])((Byte[])obj));
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer)obj);
        } else {
            return ArrayUtils.isArray(obj) ? ArrayUtils.toString(obj) : obj.toString();
        }
    }



}
