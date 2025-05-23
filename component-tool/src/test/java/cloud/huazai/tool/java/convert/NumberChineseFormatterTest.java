package cloud.huazai.tool.java.convert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberChineseFormatterTest {

    @Test
    void format() {

        double number = 999999999999.99D;
        String format = NumberChineseFormatter.format(number, false);
        System.out.println("format = " + format);

        String format1 = NumberChineseFormatter.format(number, true);
        System.out.println("format1 = " + format1);
        //
        String format2 = NumberChineseFormatter.format(number, true, true);
        System.out.println("format2 = " + format2);
        String format3 = NumberChineseFormatter.format(number, true, false);
        System.out.println("format3 = " + format3);



    }

    @Test
    void testFormat() {
    }

    @Test
    void chineseToNumber() {


        // format = 一亿二千三百四十五万六千七百八十九点一二
        // format1 = 壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾玖点壹贰
        // format2 = 壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾玖元壹角贰分
        // format3 = 壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾玖点壹贰


        System.out.println("format = " + NumberChineseFormatter.parseChinese("一二三四五点六"));
        // System.out.println("format1 = " + NumberChineseFormatter.parseChinese("一亿二千三百四十五万六千七百八十"));
        // System.out.println("format2 = " + NumberChineseFormatter.parseChinese("壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾玖元壹角贰分"));
        // System.out.println("format3 = " + NumberChineseFormatter.parseChinese("壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾元壹角贰分"));
        // System.out.println("format4 = " + NumberChineseFormatter.parseChinese("壹億贰仟叁佰肆拾伍萬陆仟柒佰捌拾元"));
    }
}