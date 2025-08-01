package cloud.huazai.dataaccesslayer.mybatis.core.handler;

import cloud.huazai.dataaccesslayer.mybatis.core.identity.UserIdGetter;

/**
 * UserIdGetterHolder
 *
 * @author Devon
 * @since 2025/7/30 12:09
 */

public class UserIdGetterHolder {

    private static volatile UserIdGetter getter;

    /**
     * 设置具体的实现（由使用者在启动时注册）
     */
    public static void setUserIdGetter(UserIdGetter getter) {
        if (getter == null) {
            throw new IllegalArgumentException("UserIdGetter cannot be null");
        }
        UserIdGetterHolder.getter = getter;
    }

    /**
     * 获取当前登录用户 ID
     */
    public static Object getUserId() {
        return getter != null ? getter.getUserId() : null;
    }

    /**
     * 判断是否已设置实现
     */
    public static boolean hasGetter() {
        return getter != null;
    }
}
