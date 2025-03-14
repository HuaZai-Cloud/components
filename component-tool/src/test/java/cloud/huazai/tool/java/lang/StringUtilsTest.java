package cloud.huazai.tool.java.lang;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void joinTest() {


        List<User> integers = new ArrayList<>();

        User user = new User();
        user.setName("test1");
        user.setAge(18);
        integers.add(user);

        User user1 = new User();
        user1.setName("test2");
        user1.setAge(19);
        integers.add(user1);

        String join = StringUtils.join(integers, ",");
        System.out.println("join = " + join);


    }
}