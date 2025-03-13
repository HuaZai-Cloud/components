package cloud.huazai.tool.java.convert;

import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Getter;

import java.util.Arrays;

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
    private static final ChineseUnit[] CHINESE_NAME_VALUE = new ChineseUnit[]{
            new ChineseUnit(' ', 1, false),
            new ChineseUnit('十', 10, false), new ChineseUnit('拾', 10, false),
            new ChineseUnit('百', 100, false), new ChineseUnit('佰', 100, false),
            new ChineseUnit('千', 1000, false), new ChineseUnit('仟', 1000, false),
            new ChineseUnit('万', 10000, true),
            new ChineseUnit('亿', 100000000, true)};


    public static String format(double amount, boolean isUseTraditional) {
        return format(amount, isUseTraditional, false);
    }

    public static String format(double amount, boolean isUseTraditional, boolean isMoneyMode) {

        // 检查金额范围
        if (amount > MAX_SUPPORTED_AMOUNT || amount < MIN_SUPPORTED_AMOUNT) {
            throw new IllegalArgumentException("Number support only: (-99999999999999.99 ～ 99999999999999.99)！");
        }

        boolean negative = amount < 0;
        if (negative) {
            amount = -amount;
        }

        long temp = Math.round(amount * 100);
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

    public static int chineseToNumber(String chinese) {
        int result = 0;
        int section = 0;
        int number = 0;
        ChineseUnit unit = null;

        for(int i = 0; i < chinese.length(); ++i) {
            char c = chinese.charAt(i);
            int num = chineseToNumber(c);
            if (num >= 0) {
                if (num == 0) {
                    if (number > 0 && null != unit) {
                        section += number * (unit.value / 10);
                    }
                    unit = null;
                } else if (number > 0) {
                    throw new IllegalArgumentException(StringUtils.format("Bad number '{}{}' at: {}", new Object[]{chinese.charAt(i - 1), c, i}));
                }

                number = num;
            } else {
                unit = chineseToUnit(c);
                if (null == unit) {
                    throw new IllegalArgumentException(StringUtils.format("Unknown unit '{}' at: {}", new Object[]{c, i}));
                }

                if (unit.secUnit) {
                    section = (section + number) * unit.value;
                    result += section;
                    section = 0;
                } else {
                    int unitNumber = number;
                    if (0 == number && 0 == i) {
                        unitNumber = 1;
                    }

                    section += unitNumber * unit.value;
                }

                number = 0;
            }
        }

        if (number > 0 && null != unit) {
            number *= (int) ((double) unit.value / 10);
        }
        return result + section + number;
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

    private static int chineseToNumber(char chinese) {
        if (chinese == LIANG) {
            chinese = TWO;
        }
        int index = Arrays.binarySearch(DIGITS, chinese);
        return index > 0 ? (index + 1) / 2 : index;
    }

    private static ChineseUnit chineseToUnit(char chinese) {
        for (ChineseUnit unit : CHINESE_NAME_VALUE) {
            if (unit.name == chinese) {
                return unit;
            }
        }
        return null;
    }

    @Getter
    private static class ChineseUnit {
        private final char name;
        private final int value;
        private final boolean secUnit;

        public ChineseUnit(char name, int value, boolean secUnit) {
            this.name = name;
            this.value = value;
            this.secUnit = secUnit;
        }
    }
}
