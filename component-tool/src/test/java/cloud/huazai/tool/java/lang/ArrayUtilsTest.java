package cloud.huazai.tool.java.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    @Test
    void isArray() {

        byte[] bytes = new byte[5];
        bytes[0] = 0;
        bytes[1] = 1;
        bytes[2] = 2;

        System.out.println("ArrayUtils.isArray(bytes) = " + ArrayUtils.isArray(bytes));


    }
}