package cloud.huazai.dataaccesslayer.sharding.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.config.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.config.rule.ReadwriteSplittingDataSourceGroupRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.transaction.config.TransactionRuleConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ShardingSphereAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/4 11:01
 */
@Configuration
@AutoConfiguration
@ConditionalOnClass(ShardingSphereDataSourceFactory.class)
@EnableConfigurationProperties(ShardingSphereProperties.class)
@ConditionalOnProperty(prefix = "dataaccesslayer.shardingsphere", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ShardingSphereAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataSource shardingSphereDataSource(ShardingSphereProperties properties) throws SQLException {
        // 如果没有配置数据源，则不创建 ShardingSphere 数据源
        if (!properties.hasDataSourceConfigured()) {
            return null;
        }

        // 构建实际的数据源
        Map<String, DataSource> dataSourceMap = createDataSourceMap(properties);

        // 构建规则配置列表
        Collection<org.apache.shardingsphere.infra.config.rule.RuleConfiguration> ruleConfigs = new ArrayList<>();

        // 如果配置了分片规则，则添加分片规则配置
        if (properties.getTables() != null && !properties.getTables().isEmpty()) {
            ShardingRuleConfiguration shardingRuleConfig = createShardingRuleConfiguration(properties);
            ruleConfigs.add(shardingRuleConfig);
        }

        // 如果配置了读写分离规则，则添加读写分离规则配置
        if (properties.hasReadwriteSplittingConfigured()) {
            ReadwriteSplittingRuleConfiguration readwriteRuleConfig = createReadwriteSplittingRuleConfiguration(properties);
            ruleConfigs.add(readwriteRuleConfig);
        }

        // // 如果配置了数据加密规则，则添加数据加密规则配置
        // if (properties.hasEncryptConfigured()) {
        //     EncryptRuleConfiguration encryptRuleConfig = createEncryptRuleConfiguration(properties);
        //     ruleConfigs.add(encryptRuleConfig);
        // }

        // 添加事务配置
        TransactionRuleConfiguration transactionRuleConfig = createTransactionRuleConfiguration(properties);
        ruleConfigs.add(transactionRuleConfig);

        // 创建 ShardingSphere 数据源
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap,
                ruleConfigs, new Properties());
    }

    private Map<String, DataSource> createDataSourceMap(ShardingSphereProperties properties) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        properties.getDatasources().forEach((name, dsConfig) -> {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(dsConfig.getDriverClassName());
            dataSource.setJdbcUrl(dsConfig.getUrl());
            dataSource.setUsername(dsConfig.getUsername());
            dataSource.setPassword(dsConfig.getPassword());
            dataSourceMap.put(name, dataSource);
        });

        return dataSourceMap;
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration(ShardingSphereProperties properties) {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        // 添加表分片规则
        properties.getTables().forEach((logicTable, tableConfig) -> {
            ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(
                    logicTable, tableConfig.getActualDataNodes());

            // 设置分片策略
            if (StringUtils.hasText(tableConfig.getTableStrategy())) {
                tableRuleConfig.setTableShardingStrategy(
                        new StandardShardingStrategyConfiguration(
                                tableConfig.getShardingColumn(),
                                tableConfig.getTableStrategy()));
            }

            if (StringUtils.hasText(tableConfig.getDatabaseStrategy())) {
                tableRuleConfig.setDatabaseShardingStrategy(
                        new StandardShardingStrategyConfiguration(
                                tableConfig.getShardingColumn(),
                                tableConfig.getDatabaseStrategy()));
            }

            shardingRuleConfig.getTables().add(tableRuleConfig);
        });

        // 添加分片算法
        properties.getShardingAlgorithms().forEach((name, algorithmConfig) -> {
            Properties props = new Properties();
            if (algorithmConfig.getProps() != null) {
                props.putAll(algorithmConfig.getProps());
            }

            shardingRuleConfig.getShardingAlgorithms().put(name,
                    new AlgorithmConfiguration(
                            algorithmConfig.getType(),
                            props));
        });

        return shardingRuleConfig;
    }

    private ReadwriteSplittingRuleConfiguration createReadwriteSplittingRuleConfiguration(ShardingSphereProperties properties) {
        Collection<ReadwriteSplittingDataSourceGroupRuleConfiguration> dataSources = new ArrayList<>();
        Map<String, AlgorithmConfiguration> loadBalancers = new HashMap<>();

        // 添加读写分离数据源规则
        properties.getReadwriteSplittingRules().forEach((name, ruleConfig) -> {
            // 解析读数据源名称
            List<String> readDataSourceNames = new ArrayList<>();
            if (StringUtils.hasText(ruleConfig.getReadDataSourceNames())) {
                readDataSourceNames = Arrays.asList(ruleConfig.getReadDataSourceNames().split(","));
                readDataSourceNames = readDataSourceNames.stream().map(String::trim).collect(Collectors.toList());
            }

            // 创建读写分离数据源组配置
            ReadwriteSplittingDataSourceGroupRuleConfiguration dataSourceRuleConfig =
                    new ReadwriteSplittingDataSourceGroupRuleConfiguration(
                            name,
                            ruleConfig.getWriteDataSourceName(),
                            readDataSourceNames,
                            ruleConfig.getLoadBalancerName());

            dataSources.add(dataSourceRuleConfig);
        });

        // 添加负载均衡算法
        loadBalancers.put("roundRobin", new AlgorithmConfiguration("ROUND_ROBIN", new Properties()));
        loadBalancers.put("random", new AlgorithmConfiguration("RANDOM", new Properties()));

        return new ReadwriteSplittingRuleConfiguration(dataSources, loadBalancers);
    }


    // private EncryptRuleConfiguration createEncryptRuleConfiguration(ShardingSphereProperties properties) {
    //     Collection<EncryptTableRuleConfiguration> tables = new ArrayList<>();
    //     Map<String, AlgorithmConfiguration> encryptors = new HashMap<>();
    //
    //     // 添加加密算法配置
    //     if (properties.getEncryptors() != null) {
    //         properties.getEncryptors().forEach((name, encryptorConfig) -> {
    //             Properties props = new Properties();
    //             if (encryptorConfig.getProps() != null) {
    //                 props.putAll(encryptorConfig.getProps());
    //             }
    //             encryptors.put(name, new AlgorithmConfiguration(
    //                     encryptorConfig.getType(), props));
    //         });
    //     }
    //
    //     // 添加加密表规则
    //     properties.getEncryptRules().forEach((tableName, ruleConfig) -> {
    //         Collection<EncryptColumnRuleConfiguration> columns = new ArrayList<>();
    //
    //         ruleConfig.getColumns().forEach((columnName, columnConfig) -> {
    //             // 创建密文列项配置
    //             EncryptColumnItemRuleConfiguration cipherItem = new EncryptColumnItemRuleConfiguration(
    //                     columnConfig.getCipherColumn(),
    //                     columnConfig.getEncryptorName());
    //
    //             // 创建加密列规则配置（只使用必要参数）
    //             EncryptColumnRuleConfiguration columnRuleConfig = new EncryptColumnRuleConfiguration(
    //                     columnName,
    //                     cipherItem);
    //
    //             columns.add(columnRuleConfig);
    //         });
    //
    //         EncryptTableRuleConfiguration tableRuleConfig = new EncryptTableRuleConfiguration(tableName, columns);
    //         tables.add(tableRuleConfig);
    //     });
    //
    //     // 如果没有定义加密算法，添加默认的AES算法
    //     if (encryptors.isEmpty()) {
    //         Properties aesProps = new Properties();
    //         aesProps.setProperty("aes-key-value", "1234567890123456");
    //         encryptors.put("AES", new AlgorithmConfiguration("AES", aesProps));
    //     }
    //
    //     return new EncryptRuleConfiguration(tables, encryptors);
    // }


    private TransactionRuleConfiguration createTransactionRuleConfiguration(ShardingSphereProperties properties) {
        ShardingSphereProperties.TransactionConfig transaction = properties.getTransaction();
        String defaultType = transaction.getType();
        String providerType = null;
        Properties props = new Properties();

        if ("XA".equals(transaction.getType())) {
            providerType = transaction.getXa().getProviderType();
            props.setProperty("xa-transaction-timeout", String.valueOf(transaction.getXa().getTimeout()));
        }

        return new TransactionRuleConfiguration(defaultType, providerType, props);
    }
}
