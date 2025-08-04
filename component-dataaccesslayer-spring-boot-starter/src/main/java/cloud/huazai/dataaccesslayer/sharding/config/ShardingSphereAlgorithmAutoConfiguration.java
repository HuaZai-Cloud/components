package cloud.huazai.dataaccesslayer.sharding.config;

import cloud.huazai.dataaccesslayer.sharding.core.algorith.ModuloShardingAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ShardingSphereAlgorithmAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/4 11:10
 */
@Configuration
@AutoConfiguration
@ConditionalOnClass(ShardingAlgorithm.class)
public class ShardingSphereAlgorithmAutoConfiguration {

    @Bean
    public ShardingAlgorithm moduloShardingAlgorithm() {
        return new ModuloShardingAlgorithm();
    }
}
