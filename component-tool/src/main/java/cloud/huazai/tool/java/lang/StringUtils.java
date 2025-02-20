package cloud.huazai.tool.java.lang;

import cloud.huazai.tool.java.constant.StringConstant;
import lombok.NonNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StringUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class StringUtils {

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

    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return "null";
        } else {
            return !ArrayUtils.isEmpty(params) && !isBlank(template) ? format(template.toString(), params) : template.toString();
        }
    }

    public static String format(String strPattern, Object... argArray) {
        if (!isBlank(strPattern) && !ArrayUtils.isEmpty(argArray)) {
            int strPatternLength = strPattern.length();
            StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
            int handledPosition = 0;

            for(int argIndex = 0; argIndex < argArray.length; ++argIndex) {
                int delimIndex = strPattern.indexOf("{}", handledPosition);
                if (delimIndex == -1) {
                    if (handledPosition == 0) {
                        return strPattern;
                    }

                    sbuf.append(strPattern, handledPosition, strPatternLength);
                    return sbuf.toString();
                }

                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == '\\') {
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == '\\') {
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(utf8Str(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        --argIndex;
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append('{');
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    sbuf.append(strPattern, handledPosition, delimIndex);
                    sbuf.append(utf8Str(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
            }

            sbuf.append(strPattern, handledPosition, strPattern.length());
            return sbuf.toString();
        } else {
            return strPattern;
        }
    }



    public static String utf8Str(Object obj) {
        return str(obj, StandardCharsets.UTF_8);
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        } else if (obj instanceof String) {
            return (String)obj;
        } else if (obj instanceof byte[]) {
            return str((byte[])((byte[])obj), charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[])((Byte[])obj), charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer)obj, charset);
        } else {
            return ArrayUtils.isArray(obj) ? ArrayUtils.toString(obj) : obj.toString();
        }
    }

    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, false);
    }

    public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        if (!isEmpty(str) && !isEmpty(searchStr)) {
            if (null == replacement) {
                replacement = "";
            }

            int strLength = str.length();
            int searchStrLength = searchStr.length();
            if (fromIndex > strLength) {
                return str(str);
            } else {
                if (fromIndex < 0) {
                    fromIndex = 0;
                }

                StringBuilder  result =  new StringBuilder(strLength + 16);

                if (0 != fromIndex) {
                    result.append(str.subSequence(0, fromIndex));
                }

                int preIndex;
                int index;
                for(preIndex = fromIndex; (index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1; preIndex = index + searchStrLength) {
                    result.append(str.subSequence(preIndex, index));
                    result.append((CharSequence)replacement);
                }

                if (preIndex < strLength) {
                    result.append(str.subSequence(preIndex, strLength));
                }

                return result.toString();
            }
        } else {
            return str(str);
        }
    }

    public static String replace(CharSequence str, int startInclude, int endExclude, char replacedChar) {
        if (isEmpty(str)) {
            return str(str);
        } else {
            int strLength = str.length();
            if (startInclude > strLength) {
                return str(str);
            } else {
                if (endExclude > strLength) {
                    endExclude = strLength;
                }

                if (startInclude > endExclude) {
                    return str(str);
                } else {
                    char[] chars = new char[strLength];

                    for(int i = 0; i < strLength; ++i) {
                        if (i >= startInclude && i < endExclude) {
                            chars[i] = replacedChar;
                        } else {
                            chars[i] = str.charAt(i);
                        }
                    }

                    return new String(chars);
                }
            }
        }
    }

    public static int indexOf(CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str != null && searchStr != null) {
            if (fromIndex < 0) {
                fromIndex = 0;
            }

            int endLimit = str.length() - searchStr.length() + 1;
            if (fromIndex > endLimit) {
                return -1;
            } else if (searchStr.length() == 0) {
                return fromIndex;
            } else if (!ignoreCase) {
                return str.toString().indexOf(searchStr.toString(), fromIndex);
            } else {
                for(int i = fromIndex; i < endLimit; ++i) {
                    if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                        return i;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        return null != str1 && null != str2 && str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

}
