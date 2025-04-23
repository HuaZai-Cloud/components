package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.constant.StringConstant;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MixedStringSortUtils {

    // 对象列表排序工具方法
    public static <T> List<T> sortListBySortField(List<T> list, Function<T, String> sortField) {
        Collator collator = Collator.getInstance(Locale.CHINA);
        collator.setStrength(Collator.PRIMARY);
        Pattern pattern = Pattern.compile("([\\p{L}]*)([\\p{N}]*)");

        return list.stream()
                .sorted(Comparator.comparing((T t) -> {
                    String key = sortField.apply(t);
                    Matcher matcher = pattern.matcher(key);
                    if (matcher.find()) {
                        String letters = matcher.group(1);
                        String numbers = matcher.group(2);
                        return new String[]{letters, numbers};
                    }
                    return new String[]{key, StringConstant.BLANK};
                }, createStringArrayComparator(collator)))
                .collect(Collectors.toList());
    }

    private static Comparator<String[]> createStringArrayComparator(Collator collator) {
        // 先创建字母部分的比较器
        Comparator<String[]> letterComparator = Comparator.comparing((String[] t) -> t[0], collator);
        // 再创建数字部分的比较器
        Comparator<String[]> numberComparator = Comparator.comparingInt(t -> {
            try {
                return t[1].isEmpty() ? 0 : Integer.parseInt(t[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        });
        // 组合两个比较器
        return letterComparator.thenComparing(numberComparator);
    }


}
