package cloud.huazai.tool.java.convert;

import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NumberChineseFormatter: Numbers to Chinese characters
 *
 * @author devon
 * @since 2025/3/13
 */
public class NumberChineseFormatter {

    private static final double MAX_SUPPORTED_AMOUNT = 9.999999999999998E11;
    private static final double MIN_SUPPORTED_AMOUNT = -9.999999999999998E11;
    private static final char ZERO = '零';
    private static final String NEGATIVE = "负";
    private static final String YUAN = "元";
    private static final String JIAO = "角";
    private static final String FEN = "分";
    private static final String DOT = "点";
    private static final String ZHENG = "整";
    private static final char LIANG = '两';
    private static final char TWO = '二';

    /**
     * Number Formatting Chinese
     *
     * @param number           number
     * @param isUseTraditional use traditional Chinese characters.
     * @return Chinese Number
     */
    public static String format(double number, boolean isUseTraditional) {
        return format(number, isUseTraditional, false);
    }

    /**
     * Number Formatting Chinese
     *
     * @param number           number
     * @param isUseTraditional use traditional Chinese characters.
     * @param isMoneyMode      is Money Mode
     * @return Chinese Number
     */
    public static String format(double number, boolean isUseTraditional, boolean isMoneyMode) {
        if (number > MAX_SUPPORTED_AMOUNT || number < MIN_SUPPORTED_AMOUNT) {
            throw new IllegalArgumentException("Number support only: (-99999999999999.99 ～ 99999999999999.99)！");
        }
        boolean negative = number < 0;
        if (negative) {
            number = -number;
        }
        long integer = 0;
        int numFen = 0;
        int numJiao = 0;
        long decimal = 0;
        if (isMoneyMode) {
            integer = Math.round(number * 100);
            numFen = (int) (integer % 10);
            integer /= 10;
            numJiao = (int) (integer % 10);
            integer /= 10;
        } else {
            BigDecimal value = new BigDecimal(Double.toString(number));
            BigDecimal integerPart = value.setScale(0, RoundingMode.DOWN); // 截断小数
            BigDecimal decimalPart = value.subtract(integerPart);
            integer = integerPart.longValue();
            String[] parts = decimalPart.toPlainString().split("\\.", 2); // 分割最多两次
            if (parts.length == 2 && !parts[1].equals("0")) {
                String decimalDigits = parts[1].replaceAll("0+$", "");
                if (StringUtils.isNotBlank(decimalDigits)) {
                    decimal = Integer.parseInt(decimalDigits);
                }
            }
        }
        StringBuilder chineseStr = new StringBuilder(longToChinese(integer, isUseTraditional));
        if (negative) {
            chineseStr.insert(0, NEGATIVE);
        }
        if (isMoneyMode) {
            if (numFen == 0 && numJiao == 0) {
                chineseStr.append(YUAN + ZHENG);
            } else if (numFen == 0) {
                chineseStr.append(YUAN).append(ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(numJiao, isUseTraditional)).append(JIAO);
            } else if (numJiao == 0) {
                chineseStr.append(YUAN + ZERO).append(ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(numFen, isUseTraditional)).append(FEN);
            } else {
                chineseStr.append(YUAN).append(ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(numJiao, isUseTraditional)).append(JIAO)
                        .append(ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(numFen, isUseTraditional)).append(FEN);
            }
        }else{
            if (decimal != 0) {
                chineseStr.append(DOT);
                ArrayList<Integer> digits = new ArrayList<>();
                while (decimal > 0) {
                    digits.add((int) (decimal % 10));  // 添加到最后一位数字
                    decimal /= 10;  // 去掉最后一位数字
                }
                if (CollectionUtils.isNotEmpty(digits)) {
                    Collections.reverse(digits);
                    for (Integer digit : digits) {
                        chineseStr.append(ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(digit, isUseTraditional));
                    }
                }
            }
        }
        return chineseStr.toString();
    }

    public static double parseChinese(String chinese) {
        boolean negative = false;
        if (chinese.startsWith(NEGATIVE)) {
            negative = true;
            chinese = chinese.substring(1).trim();
        }
        String integerPart = "";
        String decimalPart = "";
        int yuanIndex = chinese.indexOf(YUAN);
        int dotIndex = chinese.indexOf(DOT);
        if (yuanIndex != -1) {
            integerPart = chinese.substring(0, yuanIndex).trim();
            String afterYuan = chinese.substring(yuanIndex + 1).trim();
            int jiaoIndex = afterYuan.indexOf(JIAO);
            int fenIndex = afterYuan.indexOf(FEN);
            if (jiaoIndex != -1) {
                decimalPart += afterYuan.substring(0, jiaoIndex).trim();
                if (fenIndex != -1) {
                    decimalPart += afterYuan.substring(jiaoIndex + 1, fenIndex).trim();
                }
            } else if (fenIndex != -1) {
                decimalPart += afterYuan.substring(0, fenIndex).trim();
            }
        } else if (dotIndex != -1) {
            integerPart = chinese.substring(0, dotIndex).trim();
            decimalPart = chinese.substring(dotIndex + 1).trim();
        } else {
            integerPart = chinese;
        }

        // 解析整数部分
        long integerVal = parseIntegerPart(integerPart);
        // 解析小数部分
        double decimal = parseDecimalPart(decimalPart);
        // 组合整数和小数部分
        double result = integerVal + decimal;
        if (negative) {
            result = -result;
        }
        return result;
    }
    private static String longToChinese(long amount, boolean isUseTraditional) {
        if (0L == amount) {
            return String.valueOf(ZERO);
        }
        int[] parts = new int[4];
        for (int i = 0; amount != 0L; ++i) {
            parts[i] = (int) (amount % 10000L);
            amount /= 10000L;
        }
        StringBuilder chineseStr = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int partValue = parts[i];
            if (partValue > 0) {
                String partChinese = thousandToChinese(partValue, isUseTraditional);
                if (i > 0) {
                    partChinese += ChineseDigitUnit.values()[i * 2 + 4 + (isUseTraditional ? 0 : 1)].getName();
                }
                if (chineseStr.length() > 0 && partValue < 1000) {
                    addPreZero(chineseStr);
                }
                chineseStr.append(partChinese);
            } else if (chineseStr.length() > 0) {
                addPreZero(chineseStr);
            }
        }
        return chineseStr.length() > 0 && chineseStr.charAt(0) == ZERO ? chineseStr.substring(1) : chineseStr.toString();
    }

    private static void addPreZero(StringBuilder chineseStr) {
        if (chineseStr.length() > 0 && chineseStr.charAt(0) != ZERO) {
            chineseStr.insert(0, ZERO);
        }
    }
    private static String thousandToChinese(int amountPart, boolean isUseTraditional) {
        StringBuilder chineseStr = new StringBuilder();
        boolean lastIsZero = true;
        int temp = amountPart;
        for (int i = 0; temp > 0; i++) {
            int digit = temp % 10;
            if (digit == 0) {
                if (!lastIsZero) {
                    chineseStr.insert(0, String.valueOf(ZERO));
                }
                lastIsZero = true;
            } else {
                chineseStr.insert(0, ChineseDigit.getChineseDigitByDigitAndIsUseTraditional(digit, isUseTraditional) + getUnitName(i, isUseTraditional));
                lastIsZero = false;
            }
            temp /= 10;
        }
        return chineseStr.toString();
    }
    private static String getUnitName(int index, boolean isUseTraditional) {
        return index == 0 ? StringUtils.BLANK : String.valueOf(ChineseDigitUnit.values()[index * 2 - (isUseTraditional ? 2 : 1)].getName());
    }

    private static double parseDecimalPart(String decimalPart) {
        long integer = 0;
        if (StringUtils.isNotBlank(decimalPart)) {
            for (int i = 0; i < decimalPart.length(); i++) {
                char c = decimalPart.charAt(i);
                long val = 0;
                if (ChineseDigit.isChineseDigitByName(c)) {
                    val = ChineseDigit.getDigitByName(c);
                }else{
                    throw new IllegalArgumentException("Invalid number character: " + c);
                }
                integer = integer * 10 + val;
            }
        }
        double decimal = 0;
        if (integer > 0) {
            String result = "0." + integer;
            decimal = Double.parseDouble(result);
        }
        return decimal;
    }

    private static long parseIntegerPart(String str) {
        if (str.isEmpty()) {
            return 0;
        }

        List<String> groups = splitChineseNumber(str);
        long yiValue = 0;
        long wanValue = 0;
        long geValue = 0;
        for (String group : groups) {
            if (group.endsWith("亿") || group.endsWith("億")) {
                yiValue = parseSimpleGroup(group.substring(0, group.length() - 1)) * 100000000L;
            } else if (group.endsWith("万") || group.endsWith("萬")) {
                wanValue = parseSimpleGroup(group.substring(0, group.length() - 1)) * 10000L;
            } else {
                geValue = parseSimpleGroup(group);
            }
        }

        return yiValue + wanValue + geValue;
    }

    // 解析简单的一组（不带亿/万）
    private static long parseSimpleGroup(String group) {
        long result = 0;
        long current = 0;

        for (int i = 0; i < group.length(); i++) {
            char c = group.charAt(i);
            if (ChineseDigit.isChineseDigitByName(c)) {
                current = current * 10 + ChineseDigit.getDigitByName(c);
            } else if (ChineseDigitUnit.isChineseDigitUnitByName(c)) {
                int unit = ChineseDigitUnit.getDigitByName(c);
                result += current * unit;
                current = 0;
            }else{
                throw new IllegalArgumentException("Invalid number character: " + c);
            }
        }

        result += current;
        return result;
    }

    private static List<String> splitChineseNumber(String str) {
        List<String> parts = new ArrayList<>();
        StringBuilder yiBuilder = new StringBuilder();
        StringBuilder wanBuilder = new StringBuilder();
        StringBuilder geBuilder = new StringBuilder();
        boolean inYi = false;
        boolean inWan = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '亿' || c == '億') {
                yiBuilder.append(c);
                inYi = true;
                continue;
            } else if (c == '万' || c == '萬') {
                wanBuilder.append(c);
                inWan = true;
                continue;
            }
            if (!inYi && !inWan) {
                yiBuilder.append(c);
            } else if (inYi && !inWan) {
                wanBuilder.append(c);
            } else {
                geBuilder.append(c);
            }
        }
        if (yiBuilder.length() > 0) {
            parts.add(yiBuilder.toString());
        }
        if (wanBuilder.length() > 0) {
            parts.add(wanBuilder.toString());
        }
        if (geBuilder.length() > 0) {
            parts.add(geBuilder.toString());
        }
        return parts;
    }


}
