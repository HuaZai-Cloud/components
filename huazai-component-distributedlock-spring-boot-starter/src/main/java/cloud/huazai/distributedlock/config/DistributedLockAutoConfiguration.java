package cloud.huazai.distributedlock.config;

import cloud.huazai.distributedlock.aspect.DistributedLockAspect;
import cloud.huazai.tool.java.constant.StringConstant;
import cloud.huazai.tool.java.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedisLockAutoConfiguration
 *
 * @author huazai
 * @since 2024/12/20
 */
@Configuration
@EnableConfigurationProperties(DistributedLockProperties.class)
@ConditionalOnProperty(prefix = "spring.redis", name = "host")
public class DistributedLockAutoConfiguration {

    private static final String SERVER_CONFIG_ADDRESS_PREFIX = "redis://";

    private final DistributedLockProperties redisLockProperties;

    public DistributedLockAutoConfiguration(DistributedLockProperties redisLockProperties) {
        this.redisLockProperties = redisLockProperties;
    }


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(SERVER_CONFIG_ADDRESS_PREFIX + redisLockProperties.getHost() + StringConstant.COLON + redisLockProperties.getPort());
        singleServerConfig.setPingConnectionInterval(5000);
        if (StringUtils.isNotBlank(redisLockProperties.getPassword())) {
            singleServerConfig.setPassword(redisLockProperties.getPassword());
        }
        // 设置线程池参数
        //设置传输模式和线程池参数
        config.setNettyThreads(8);
        return Redisson.create(config);
    }

    @Bean
    public DistributedLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new DistributedLockAspect(redissonClient);
    }
}
