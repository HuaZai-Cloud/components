package cloud.huazai.tool.java.lang;

/**
 * StringUtils
 *
 * @author devon
 * @since 2024/12/11
 */

public class StringUtils {

    public static boolean isBlank(CharSequence cs) {
        int strLength = length(cs);
        if (strLength == 0) {
            return true;
        } else {
            for(int i = 0; i < strLength; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
