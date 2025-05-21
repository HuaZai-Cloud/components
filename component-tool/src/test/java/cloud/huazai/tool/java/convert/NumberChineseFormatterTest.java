package cloud.huazai.tool.java.convert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberChineseFormatterTest {

    @Test
    void format() {

        double number = 123456789.12;
        String format = NumberChineseFormatter.format(number, false);
        System.out.println("format = " + format);
        double number1 = 12345678.12;
        String format1 = NumberChineseFormatter.format(number1, false);
        System.out.println("format1 = " + format1);
        // String format1 = NumberChineseFormatter.format(number, false);
        // System.out.println("format1 = " + format1);
        // String format2 = NumberChineseFormatter.format(number, true, true);
        // System.out.println("format2 = " + format2);
        // String format3 = NumberChineseFormatter.format(number, true, false);
        // System.out.println("format3 = " + format3);



        // 一千二百三十四万五千六百七十八点一二


/*         format = 壹千贰万叁千肆百伍百陆万柒千捌百玖点壹贰
        format1 = 一仟二仟三佰四拾五佰六仟七佰八拾九点一二
        format2 = 壹千贰万叁千肆百伍百陆万柒千捌百玖元壹角贰分
        format3 = 壹千贰万叁千肆百伍百陆万柒千捌百玖点壹贰 */

    }

    @Test
    void testFormat() {
    }

    @Test
    void chineseToNumber() {
        // System.out.println("true = " + true);
        //
        // String chinese = "壹佰贰拾叁元肆角伍分";
        // double parse = NumberChineseFormatter.parseChinese(chinese);
        // System.out.println("parse = " + parse);


        System.out.println("format = " + NumberChineseFormatter.parseChinese("一千二百三十四万五千六百七十八点一二"));
        // System.out.println("format1 = " + NumberChineseFormatter.parseChinese("一仟二仟三佰四拾五佰六仟七佰八拾九点一二"));
        // System.out.println("format2 = " + NumberChineseFormatter.parseChinese("壹千贰万叁千肆百伍百陆万柒千捌百玖元壹角贰分"));
        // System.out.println("format3 = " + NumberChineseFormatter.parseChinese("壹千贰万叁千肆百伍百陆万柒千捌百玖点壹贰"));
    }
}