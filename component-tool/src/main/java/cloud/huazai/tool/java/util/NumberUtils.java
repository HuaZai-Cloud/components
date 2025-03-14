package cloud.huazai.tool.java.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    public static Number parseNumber(String value, Class<?> clazz) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            if (clazz == Integer.class || clazz == int.class) {
                return Integer.parseInt(value);
            } else if (clazz == Long.class || clazz == long.class) {
                return Long.parseLong(value);
            } else if (clazz == Float.class || clazz == float.class) {
                return Float.parseFloat(value);
            } else if (clazz == Double.class || clazz == double.class) {
                return Double.parseDouble(value);
            } else if (clazz == Short.class || clazz == short.class) {
                return Short.parseShort(value);
            } else if (clazz == Byte.class || clazz == byte.class) {
                return Byte.parseByte(value);
            } else if (clazz == BigInteger.class) {
                return new BigInteger(value);
            } else if (clazz == BigDecimal.class) {
                return new BigDecimal(value);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse number: " + value, e);
        }
        throw new IllegalArgumentException("Unsupported number type: " + clazz.getName());
    }
}
