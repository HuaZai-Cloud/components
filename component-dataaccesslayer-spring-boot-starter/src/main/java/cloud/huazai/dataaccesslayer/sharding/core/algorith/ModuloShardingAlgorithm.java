package cloud.huazai.dataaccesslayer.sharding.core.algorith;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * ModuloShardingAlgorithm
 *
 * @author Devon
 * @since 2025/8/4 11:09
 */

public class ModuloShardingAlgorithm implements StandardShardingAlgorithm<Long> {
    private Properties props = new Properties();

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long value = shardingValue.getValue();
        String suffix = String.valueOf(value.hashCode() % availableTargetNames.size());
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(suffix)) {
                return tableName;
            }
        }
        throw new IllegalArgumentException("No matching table found for sharding value: " + value);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        return availableTargetNames;
    }


    @Override
    public String getType() {
        return "MODULO";
    }


}
