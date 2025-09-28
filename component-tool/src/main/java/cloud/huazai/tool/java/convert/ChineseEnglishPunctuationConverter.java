package cloud.huazai.tool.java.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * ChineseEnglishPunctuationConverter
 *
 * @author Devon
 * @since 2025/9/28 14:42
 */

public class ChineseEnglishPunctuationConverter {

    // 全角（中文）标点 -> 半角（英文）标点 映射表
    private static final Map<Character, Character> FULL_TO_HALF = new HashMap<>();
    // 半角（英文）标点 -> 全角（中文）标点 映射表
    private static final Map<Character, Character> HALF_TO_FULL = new HashMap<>();

    static {
        // 常见中文全角标点及其对应的英文半角
        FULL_TO_HALF.put('，', ',');
        FULL_TO_HALF.put('。', '.');
        FULL_TO_HALF.put('！', '!');
        FULL_TO_HALF.put('？', '?');
        FULL_TO_HALF.put('；', ';');
        FULL_TO_HALF.put('：', ':');
        FULL_TO_HALF.put('“', '"');
        FULL_TO_HALF.put('”', '"');
        FULL_TO_HALF.put('‘', '\'');
        FULL_TO_HALF.put('’', '\'');
        FULL_TO_HALF.put('【', '[');
        FULL_TO_HALF.put('】', ']');
        FULL_TO_HALF.put('（', '(');
        FULL_TO_HALF.put('）', ')');
        FULL_TO_HALF.put('《', '<');
        FULL_TO_HALF.put('》', '>');
        // FULL_TO_HALF.put('……', '...'); // 特殊处理：省略号
        FULL_TO_HALF.put('—', '-');     // 破折号转为连字符改45
        FULL_TO_HALF.put('、', ',');    // 顿号转为逗号（可自定义）
        FULL_TO_HALF.put('·', '.');     // 着重号转为点（可自定义）

        // 构建反向映射
        for (Map.Entry<Character, Character> entry : FULL_TO_HALF.entrySet()) {
            char half = entry.getValue();
            char full = entry.getKey();
            // 注意：多个全角可能映射到同一个半角（如“”都转为"），所以反向映射时需注意
            if (!HALF_TO_FULL.containsKey(half)) {
                HALF_TO_FULL.put(half, full);
            }
        }

        // 手动补充一些半角到全角的映射（避免歧义）
        HALF_TO_FULL.put(',', '，');
        HALF_TO_FULL.put('.', '。'); // 注意：点号根据语境可能是句号或小数点
        HALF_TO_FULL.put('!', '！');
        HALF_TO_FULL.put('?', '？');
        HALF_TO_FULL.put(';', '；');
        HALF_TO_FULL.put(':', '：');
        HALF_TO_FULL.put('[', '【');
        HALF_TO_FULL.put(']', '】');
        HALF_TO_FULL.put('(', '（');
        HALF_TO_FULL.put(')', '）');
        HALF_TO_FULL.put('<', '《');
        HALF_TO_FULL.put('>', '》');
        HALF_TO_FULL.put('-', '—'); // 连字符转为破折号（可选）
        HALF_TO_FULL.put('\'', '‘'); // 单引号默认转左引号
        HALF_TO_FULL.put('"', '“'); // 双引号默认转左引号
    }

    /**
     * 将字符串中的中文全角标点转换为英文半角标点
     * @param text 输入文本
     * @return 转换后的文本
     */
    public static String convertFullToHalf(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (FULL_TO_HALF.containsKey(c)) {
                sb.append(FULL_TO_HALF.get(c));
            } else {
                // 处理全角字母/数字（Unicode范围：\uFF01-\uFF5E）
                if (c >= '\uFF01' && c <= '\uFF5E') {
                    sb.append((char)(c - 65248)); // 转换为半角
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串中的英文半角标点转换为中文全角标点
     * @param text 输入文本
     * @return 转换后的文本
     */
    public static String convertHalfToFull(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (HALF_TO_FULL.containsKey(c)) {
                sb.append(HALF_TO_FULL.get(c));
            } else if (c >= '!' && c <= '~') {
                // 转换半角字母数字标点为全角
                sb.append((char)(c + 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
