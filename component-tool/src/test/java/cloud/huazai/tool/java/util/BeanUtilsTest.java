package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.User;
import org.junit.jupiter.api.Test;

class BeanUtilsTest {

    @Test
    void copyProperties() {


        User user = new User();
        user.setName("huazai");
        user.setAge(18);

        User user1 = new User();

        BeanUtils.copyProperties(user, user1,"age");

        System.out.println("user1 = " + user1);






    }
}