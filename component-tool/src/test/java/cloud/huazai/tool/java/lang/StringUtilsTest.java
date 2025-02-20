package cloud.huazai.tool.java.lang;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StringUtilsTest
 *
 * @author devon
 * @since 2025/1/16
 */

class StringUtilsTest {

    @Test
    void split() {

        String str = "1,2,3,4";
        List<String> split = StringUtils.split(str, ",");
        System.out.println("split = " + split);

        List<String> split1 = StringUtils.split(str, null);
        System.out.println("split1 = " + split1);


    }

    @Test
    void join() {

        ArrayList<Integer> integers = new ArrayList<>();


        String join = StringUtils.join(integers, null);
        System.out.println("join = " + join);

    }
}