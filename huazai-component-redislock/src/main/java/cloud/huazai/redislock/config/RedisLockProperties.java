package cloud.huazai.redislock.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RedisLockProperties
 *
 * @author devon
 * @since 2024/12/20
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedisLockProperties {

    private String host;
    private String port;
    private String password;

}
