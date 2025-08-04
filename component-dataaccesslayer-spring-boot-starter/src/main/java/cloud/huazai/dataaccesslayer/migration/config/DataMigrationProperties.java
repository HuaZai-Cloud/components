package cloud.huazai.dataaccesslayer.migration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * DataMigrationProperties
 *
 * @author Devon
 * @since 2025/8/4 15:24
 */
@Data
@ConfigurationProperties(prefix = "dataaccesslayer.migration")
public class DataMigrationProperties {
    /**
     * 是否启用数据迁移
     */
    private boolean enabled = false;

    /**
     * 迁移脚本位置
     */
    private List<String> locations = new ArrayList<>();

    /**
     * 是否在迁移时执行 baseline
     */
    private boolean baselineOnMigrate = true;

    /**
     * baseline 版本
     */
    private String baselineVersion = "0";

    /**
     * 脚本编码
     */
    private String encoding = "UTF-8";

    /**
     * 是否允许无序执行
     */
    private boolean outOfOrder = false;

    /**
     * 表前缀
     */
    private String tablePrefix = "flyway_";

    /**
     * 是否启用自动迁移
     */
    private boolean autoMigrate = false;

    public DataMigrationProperties() {
        locations.add("classpath:db/migration");
    }
}
