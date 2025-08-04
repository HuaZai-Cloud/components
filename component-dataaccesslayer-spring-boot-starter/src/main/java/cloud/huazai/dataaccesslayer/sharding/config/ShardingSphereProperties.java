package cloud.huazai.dataaccesslayer.sharding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * ShardingSphereProperties
 *
 * @author Devon
 * @since 2025/8/4 11:07
 */
@Data
@ConfigurationProperties(prefix = "shardingsphere")
public class ShardingSphereProperties {

    /**
     * 是否启用 ShardingSphere
     */
    private boolean enabled = true;

    /**
     * 数据源配置
     */
    private Map<String, DataSourceConfig> datasources = new HashMap<>();

    /**
     * 分片表配置
     */
    private Map<String, TableRuleConfig> tables = new HashMap<>();

    /**
     * 分片算法配置
     */
    private Map<String, AlgorithmConfig> shardingAlgorithms = new HashMap<>();

    /**
     * 读写分离配置
     */
    private Map<String, ReadwriteSplittingRuleConfig> readwriteSplittingRules = new HashMap<>();

    // /**
    //  * 数据加密配置
    //  */
    // private Map<String, EncryptRuleConfig> encryptRules = new HashMap<>();
    //
    // // 添加加密算法配置
    // private Map<String, EncryptorConfig> encryptors = new HashMap<>();

    /**
     * 分布式事务配置
     */
    private TransactionConfig transaction = new TransactionConfig();

    @Data
    public static class DataSourceConfig {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String type = "com.zaxxer.hikari.HikariDataSource";
    }

    @Data
    public static class TableRuleConfig {
        private String actualDataNodes;
        private String shardingColumn;
        private String tableStrategy;
        private String databaseStrategy;
    }

    @Data
    public static class AlgorithmConfig {
        private String type;
        private Map<String, String> props = new HashMap<>();
    }

    @Data
    public static class ReadwriteSplittingRuleConfig {
        private String writeDataSourceName;
        private String readDataSourceNames;
        private String loadBalancerName = "roundRobin";
    }

    // // 在 ShardingSphereProperties 中添加加密算法配置
    // @Data
    // public static class EncryptRuleConfig {
    //     private String tableName;
    //     private Map<String, EncryptColumnConfig> columns = new HashMap<>();
    // }
    //
    // @Data
    // public static class EncryptColumnConfig {
    //     private String plainColumn;
    //     private String cipherColumn;
    //     private String encryptorName;
    //     private String assistedQueryColumn;
    // }

    @Data
    public static class TransactionConfig {
        private String type = "LOCAL"; // LOCAL, XA, BASE
        private XAConfig xa = new XAConfig();
    }

    @Data
    public static class XAConfig {
        private String providerType = "Atomikos";
        private int timeout = 60;
    }

    @Data
    public static class EncryptorConfig {
        private String type;  // 算法类型，如 AES, MD5 等
        private Map<String, String> props = new HashMap<>(); // 算法属性
    }



    /**
     * 判断是否已配置数据源
     */
    public boolean hasDataSourceConfigured() {
        return datasources != null && !datasources.isEmpty();
    }

    /**
     * 判断是否配置了读写分离
     */
    public boolean hasReadwriteSplittingConfigured() {
        return readwriteSplittingRules != null && !readwriteSplittingRules.isEmpty();
    }

    // /**
    //  * 判断是否配置了数据加密
    //  */
    // public boolean hasEncryptConfigured() {
    //     return (encryptRules != null && !encryptRules.isEmpty()) ||
    //             (encryptors != null && !encryptors.isEmpty());
    // }
}
