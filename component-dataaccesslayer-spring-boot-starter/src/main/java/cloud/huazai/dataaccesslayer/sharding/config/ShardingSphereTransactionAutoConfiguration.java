package cloud.huazai.dataaccesslayer.sharding.config;

import org.apache.shardingsphere.transaction.api.TransactionType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * ShardingSphereTransactionAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/4 13:38
 */
@AutoConfiguration
@ConditionalOnClass(TransactionType.class)
@EnableTransactionManagement
public class ShardingSphereTransactionAutoConfiguration {
}
