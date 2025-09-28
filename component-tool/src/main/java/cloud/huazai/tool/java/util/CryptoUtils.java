package cloud.huazai.tool.java.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * CryptoUtils
 *
 * @author Devon
 * @since 2025/9/28 15:08
 */

public class CryptoUtils {

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 不可用", e);
        }
    }

    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String base64Decode(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }
}
