package cloud.huazai.tool.java.util;

import lombok.NonNull;

import java.util.Random;

public class RandomUtils {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = LOWERCASE.toUpperCase();
    private static final String DIGITS = "0123456789";

    private static final String LOWERCASE_DIGITS = LOWERCASE + DIGITS;
    private static final String UPPERCASE_DIGITS = UPPERCASE + DIGITS;
    private static final String UPPERCASE_LOWERCASE = LOWERCASE + UPPERCASE;
    private static final String UPPERCASE_LOWERCASE_DIGITS = LOWERCASE + UPPERCASE + DIGITS;

    private static final Random RANDOM = new Random();


    public static String getRandomLowercase(int length) {
        return getRandom(LOWERCASE, length);
    }

    public static String getRandomUppercase(int length) {
        return getRandom(UPPERCASE, length);
    }


    public static String getRandomDigits(int length) {
        return getRandom(DIGITS, length);
    }

    public static String getRandomLowercaseDigits(int length) {
        return getRandom(LOWERCASE_DIGITS, length);
    }


    public static String getRandomUppercaseDigits(int length) {
        return getRandom(UPPERCASE_DIGITS, length);
    }


    public static String getRandomUppercaseLowercase(int length) {
        return getRandom(UPPERCASE_LOWERCASE, length);
    }


    public static String getRandomUppercaseLowercaseDigits(int length) {
        return getRandom(UPPERCASE_LOWERCASE_DIGITS, length);
    }

    public static String getRandomString(@NonNull String characterSet, int length) {
        return getRandom(characterSet, length);
    }

    private static String getRandom(String characterSet, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characterSet.length());
            result.append(characterSet.charAt(index));
        }
        return result.toString();
    }
}
