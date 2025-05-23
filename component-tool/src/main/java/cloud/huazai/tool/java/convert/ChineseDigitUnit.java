package cloud.huazai.tool.java.convert;

import lombok.Getter;

@Getter
public enum ChineseDigitUnit {

    TRADITIONAL_TEN('拾', 10,true),
    SIMPLIFIED_TEN('十', 10,false),
    TRADITIONAL_HUNDRED('佰', 100,true),
    SIMPLIFIED_HUNDRED('百', 100,false),
    TRADITIONAL_THOUSAND('仟', 1000,true),
    SIMPLIFIED_THOUSAND('千', 1000,false),
    TRADITIONAL_TEN_THOUSAND('萬',  10000,true),
    SIMPLIFIED_TEN_THOUSAND('万',10000,false),
    TRADITIONAL_HUNDRED_MILLION('億', 100000000,true),
    SIMPLIFIED_HUNDRED_MILLION( '亿',100000000,false),

    ;

    private final char name;
    private final int value;
    private final boolean isUseTraditional;

    ChineseDigitUnit(char name, int value, boolean isUseTraditional) {
        this.name = name;
        this.value = value;
        this.isUseTraditional = isUseTraditional;
    }

    public static boolean isChineseDigitUnitByName(char name) {
        for (ChineseDigitUnit chineseDigit : values()) {
            if (chineseDigit.name == name) {
                return true;
            }
        }
        return false;
    }

    public static Integer getDigitByName(char name) {
        Integer result = null;
        for (ChineseDigitUnit chineseDigit : values()) {
            if (chineseDigit.name == name) {
                result = chineseDigit.value;
            }
        }
        return result;
    }

    public static String getChineseDigitUnitByDigitAndIsUseTraditional(int digit,boolean isUseTraditional) {
        String result = "";
        for (ChineseDigitUnit chineseDigit : values()) {
            if (chineseDigit.isUseTraditional == isUseTraditional && chineseDigit.value == digit) {
                result = chineseDigit.name();
            }
        }
        return result;
    }

}
