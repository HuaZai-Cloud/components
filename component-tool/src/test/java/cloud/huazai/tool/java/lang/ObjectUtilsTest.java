package cloud.huazai.tool.java.lang;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    @Test
    void testToString() {

        List<User> integers = new ArrayList<>();

        User user = new User();
        user.setName("test1");
        user.setAge(18);
        integers.add(user);

        User user1 = new User();
        user1.setName("test2");
        user1.setAge(19);
        integers.add(user1);

        ObjectUtils.toString(integers);

    }
}