package cloud.huazai.tool.java.convert;

import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Getter;

import java.util.Arrays;

/**
 * NumberChineseFormatter: Numbers to Chinese characters
 *
 * @author devon
 * @since 2025/3/13
 */
public class NumberChineseFormatter {

    private static final double MAX_SUPPORTED_AMOUNT = 9.999999999999998E13;
    private static final double MIN_SUPPORTED_AMOUNT = -9.999999999999998E13;
    private static final char ZERO = '零';
    private static final String NEGATIVE = "负";
    private static final String YUAN = "元";
    private static final String JIAO = "角";
    private static final String FEN = "分";
    private static final String DOT = "点";
    private static final String ZHENG = "整";
    private static final char LIANG = '两';
    private static final char TWO = '二';

    private static final char[] DIGITS = new char[]{
            '零',
            '一', '壹',
            '二', '贰',
            '三', '叁',
            '四', '肆',
            '五', '伍',
            '六', '陆',
            '七', '柒',
            '八', '捌',
            '九', '玖'
    };


    private static final ChineseUnit[] DIGITS_VALUE = new ChineseUnit[]{
            new ChineseUnit('零', 0),
            new ChineseUnit('一', 1), new ChineseUnit('壹', 1),
            new ChineseUnit('二', 2), new ChineseUnit('贰', 2),
            new ChineseUnit('三', 3), new ChineseUnit('叁', 3),
            new ChineseUnit('四', 4), new ChineseUnit('肆', 4),
            new ChineseUnit('五', 5), new ChineseUnit('伍', 5),
            new ChineseUnit('六', 6), new ChineseUnit('陆', 6),
            new ChineseUnit('七', 7), new ChineseUnit('柒', 7),
            new ChineseUnit('八', 8), new ChineseUnit('捌', 8),
            new ChineseUnit('九', 9), new ChineseUnit('玖', 9),

    };

    private static final ChineseUnit[] CHINESE_NAME_VALUE = new ChineseUnit[]{
            new ChineseUnit(' ', 1),
            new ChineseUnit('十', 10), new ChineseUnit('拾', 10),
            new ChineseUnit('百', 100), new ChineseUnit('佰', 100),
            new ChineseUnit('千', 1000), new ChineseUnit('仟', 1000),
            new ChineseUnit('万', 10000),  new ChineseUnit('萬', 10000),
            new ChineseUnit('亿', 100000000),new ChineseUnit('億', 100000000),
    };


    /**
     * Number Formatting Chinese
     * @param number number
     * @param isUseTraditional  use traditional Chinese characters.
     * @return Chinese Number
     */
    public static String format(double number, boolean isUseTraditional) {
        return format(number, isUseTraditional, false);
    }

    /**
     * Number Formatting Chinese
     * @param number number
     * @param isUseTraditional use traditional Chinese characters.
     * @param isMoneyMode is Money Mode
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

        long temp = Math.round(number * 100);
        int numFen = (int) (temp % 10);
        temp /= 10;
        int numJiao = (int) (temp % 10);
        temp /= 10;

        StringBuilder chineseStr = new StringBuilder(longToChinese(temp, isUseTraditional));
        if (negative) {
            chineseStr.insert(0, NEGATIVE);
        }

        if (numFen == 0 && numJiao == 0) {
            if (isMoneyMode) {
                chineseStr.append(YUAN+ZHENG);
            }
        } else if (numFen == 0) {
            chineseStr.append(isMoneyMode ? YUAN : DOT)
                    .append(numberToChinese(numJiao, isUseTraditional))
                    .append(isMoneyMode ? JIAO : StringUtils.BLANK);
        } else if (numJiao == 0) {
            chineseStr.append(isMoneyMode ? YUAN+ZERO : DOT+ZERO)
                    .append(numberToChinese(numFen, isUseTraditional))
                    .append(isMoneyMode ? FEN : StringUtils.BLANK);
        } else {
            chineseStr.append(isMoneyMode ? YUAN : DOT)
                    .append(numberToChinese(numJiao, isUseTraditional))
                    .append(isMoneyMode ? JIAO : StringUtils.BLANK)
                    .append(numberToChinese(numFen, isUseTraditional))
                    .append(isMoneyMode ? FEN : StringUtils.BLANK);
        }
        return chineseStr.toString();
    }


    private static String longToChinese(long amount, boolean isUseTraditional) {
        if (0L == amount) {
            return String.valueOf(ZERO);
        }
            int[] parts = new int[4];

            for(int i = 0; amount != 0L; ++i) {
                parts[i] = (int)(amount % 10000L);
                amount /= 10000L;
            }

            StringBuilder chineseStr = new StringBuilder();

        for (int i = 3; i >= 0; i--) {
            int partValue = parts[i];
            if (partValue > 0) {
                String partChinese = thousandToChinese(partValue, isUseTraditional);
                if (i > 0) {
                    partChinese += CHINESE_NAME_VALUE[i * 2 + (isUseTraditional ? 0 : 1)].name;
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

    private static char numberToChinese(int number, boolean isUseTraditional) {
        return 0 == number ? DIGITS[0] : DIGITS[number * 2 - (isUseTraditional ? 0 : 1)];
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
                chineseStr.insert(0, numberToChinese(digit, isUseTraditional) + getUnitName(i, isUseTraditional));
                lastIsZero = false;
            }
            temp /= 10;
        }
        return chineseStr.toString();
    }

    private static String getUnitName(int index, boolean isUseTraditional) {
        return index == 0 ? StringUtils.BLANK : String.valueOf(CHINESE_NAME_VALUE[index * 2 - (isUseTraditional ? 0 : 1)].name);
    }


    @Getter
    private static class ChineseUnit {
        private final char name;
        private final int value;
        // private final boolean secUnit;

        // public ChineseUnit(char name, int value, boolean secUnit) {
        public ChineseUnit(char name, int value) {
            this.name = name;
            this.value = value;
            // this.secUnit = secUnit;
        }

    }

    private static boolean isContainByName(ChineseUnit[] units, char name) {
        return Arrays.stream(units).anyMatch(unit -> unit.name == name);
    }

    private static Integer getNumberByName(ChineseUnit[] units, char name) {
        Integer result = null;
        for (ChineseUnit unit : units) {
            if (unit.name == name) {
                result = unit.value;
            }
        }
        return result;
    }



    public static double parseChinese(String chinese) {

        // 处理负号
        boolean negative = false;
        if (chinese.startsWith(NEGATIVE)) {
            negative = true;
            chinese = chinese.substring(1).trim();
        }

        // 处理货币模式：分割整数和小数部分
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
                // 没有“元”字，整数部分是整个字符串？
                integerPart = chinese;
            }

        // 解析整数部分
        long integerVal = parseIntegerPart(integerPart);

        // 解析小数部分
        double decimal = parseDecimalPart(decimalPart);

        // 组合整数和小数部分
        double result = integerVal + decimal ;

        if (negative) {
            result = -result;
        }

        return result;
    }

    private static double parseDecimalPart(String decimalPart) {
        long integer = 0;
        if (StringUtils.isNotBlank(decimalPart)) {
            for (int i = 0; i < decimalPart.length(); i++) {
                char c = decimalPart.charAt(i);
                long val = 0;
                if (isNumberChar(c)) {
                    val  = numberCharToValue(c);
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

        // 将中文字符转换为数字和单位
        // 初始化变量
        long total = 0;
        int currentNumber = 0;
        boolean isFirstUnit = true;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (isNumberChar(c)) {
                currentNumber += numberCharToValue(c);
                isFirstUnit = true;
            } else if (isUnitChar(c)) {
                int unitValue = unitCharToValue(c);

                if (isFirstUnit) {
                    if (currentNumber == 0) {
                        total +=  unitValue;
                    }else {
                        total += (long) currentNumber * unitValue;
                    }
                    isFirstUnit = false;
                }else{
                    total = (total + currentNumber) * unitValue;
                }
                currentNumber = 0;

                // if (currentNumber == 0) {
                //     if (isFirstUnit) {
                //         total +=  unitValue;
                //         isFirstUnit = false;
                //     }
                // } else {
                //     total += (long) currentNumber * unitValue;
                //     currentNumber = 0;
                // }
            } else if (c == ZERO) {
                currentNumber += 0;
            } else {
                // 未知字符，可能抛出异常？
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        // 处理最后的currentNumber
        total += currentNumber;

        return total;
    }

    // 辅助方法：判断字符是否是数字字符
    private static boolean isNumberChar(char c) {
        return isContainByName(DIGITS_VALUE, c);
    }

    // 将数字字符转换为对应的数值
    private static int numberCharToValue(char c) {
        // int index;

        Integer number = getNumberByName(DIGITS_VALUE, c);
        if (number == null) {
            throw new IllegalArgumentException("Invalid number character: " + c);
        }

        return number;

    }

    // 判断字符是否是单位字符
    private static boolean isUnitChar(char c) {
        return isContainByName(CHINESE_NAME_VALUE, c);
    }

    // 将单位字符转换为对应的数值
    private static int unitCharToValue(char c) {

        Integer number = getNumberByName(CHINESE_NAME_VALUE, c);

        if (number == null) {
            throw new IllegalArgumentException("Invalid unit character: " + c);
        }

        return number;
    }














}
