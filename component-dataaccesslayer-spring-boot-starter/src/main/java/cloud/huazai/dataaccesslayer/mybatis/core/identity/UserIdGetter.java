package cloud.huazai.dataaccesslayer.mybatis.core.identity;

/**
 * UserIdGetter
 *
 * @author Devon
 * @since 2025/7/30 12:08
 */
@FunctionalInterface
public interface UserIdGetter {
    /**
     * 获取当前登录用户的 ID
     * @return 用户ID，如果没有登录则返回 null
     */
    Object getUserId();
}
