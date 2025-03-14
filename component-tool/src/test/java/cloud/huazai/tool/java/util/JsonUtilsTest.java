package cloud.huazai.tool.java.util;

import cloud.huazai.tool.java.lang.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JsonUtilsTest {

    @Test
    void toJsonString() {
    }

    @Test
    void testToJsonString() {

        List<User> integers = new ArrayList<>();

        User user = new User();
        user.setName("test1");
        user.setAge(18);
        integers.add(user);

        User user1 = new User();
        user1.setName("test2");
        user1.setAge(19);
        integers.add(user1);

        String json = JsonUtils.toJsonString(integers);
        System.out.println("json = " + json);


        List<User> userList = JsonUtils.parseArray(json, User.class);

        userList.forEach(System.out::println);

        String jsonString = JsonUtils.toJsonString(user);
        System.out.println("jsonString = " + jsonString);
        User user2 = JsonUtils.parseObject(jsonString, User.class);
        System.out.println("user2 = " + user2);

        System.out.println("===========================================");


        Map<String, Object> map = new HashMap<>();
        map.put("name", "test1");
        map.put("age", 18);
        String jsonString1 = JsonUtils.toJsonString(map);
        System.out.println("jsonString1 = " + jsonString1);

//        Map<String, String> stringStringMap = JsonUtils.parseMap(jsonString1);
//        System.out.println("stringStringMap.get(\"name\") = " + stringStringMap.get("name"));
//
//        System.out.println("stringStringMap.get(\"age\") = " + stringStringMap.get("age"));

//        String age = stringStringMap.get("age");

    }
}