package cloud.huazai.tool.java.convert;

import lombok.Getter;

@Getter
public enum ChineseDigit {

    TRADITIONAL_ZERO('零', 0,true),
    SIMPLIFIED_ZERO('零', 0,false),
    TRADITIONAL_ONE('壹', 1,true),
    SIMPLIFIED_ONE('一', 1,false),
    TRADITIONAL_TWO('贰', 2,true),
    SIMPLIFIED_TWO('二', 2,false),
    TRADITIONAL_THREE('叁',  3,true),
    SIMPLIFIED_THREE('三',3,false),
    TRADITIONAL_FOUR('肆', 4,true),
    SIMPLIFIED_FOUR( '四',4,false),
    TRADITIONAL_FIVE('伍', 5,true),
    SIMPLIFIED_FIVE( '五',5,false),
    TRADITIONAL_SIX('陆',  6,true),
    SIMPLIFIED_SIX('六',6,false),
    TRADITIONAL_SEVEN('柒', 7,true),
    SIMPLIFIED_SEVEN( '七',7,false),
    TRADITIONAL_EIGHT('捌', 8,true),
    SIMPLIFIED_EIGHT( '八',8,false),
    TRADITIONAL_NINE('玖', 9,true),
    SIMPLIFIED_NINE( '九',9,false),

    ;

    private final char name;
    private final int value;
    private final boolean isUseTraditional;

    ChineseDigit(char name, int value,boolean isUseTraditional) {
        this.name = name;
        this.value = value;
        this.isUseTraditional = isUseTraditional;
    }

    private static boolean isChineseDigitByName(char name) {
        for (ChineseDigit chineseDigit : values()) {
            if (chineseDigit.name == name) {
                return true;
            }
        }
        return false;
    }

    private static Integer getDigitByName(char name) {
        Integer result = null;
        for (ChineseDigit chineseDigit : values()) {
            if (chineseDigit.name == name) {
                result = chineseDigit.value;
            }
        }
        return result;
    }

    private static String getChineseDigitByDigitAndIsUseTraditional(int digit,boolean isUseTraditional) {
        String result = "";
        for (ChineseDigit chineseDigit : values()) {
            if (chineseDigit.isUseTraditional == isUseTraditional && chineseDigit.value == digit) {
                result = chineseDigit.name();
            }
        }
        return result;
    }

}
