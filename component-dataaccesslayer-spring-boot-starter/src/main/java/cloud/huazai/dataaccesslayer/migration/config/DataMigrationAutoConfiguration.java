package cloud.huazai.dataaccesslayer.migration.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * DataMigrationAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/4 15:25
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "dataaccesslayer.migration", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DataMigrationProperties.class)
public class DataMigrationAutoConfiguration {

    private final DataMigrationProperties migrationProperties;

    /**
     * 创建Flyway Bean用于数据迁移
     *
     * @param dataSource 数据源
     * @return Flyway实例
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    public Flyway flyway(DataSource dataSource) {
        // log.info("Initializing Flyway for data migration...");

        FluentConfiguration configuration = Flyway.configure()
                .dataSource(dataSource)
                .locations(migrationProperties.getLocations().toArray(new String[0]))
                .baselineOnMigrate(migrationProperties.isBaselineOnMigrate())
                .baselineVersion(migrationProperties.getBaselineVersion())
                .encoding(migrationProperties.getEncoding())
                .outOfOrder(migrationProperties.isOutOfOrder())
                .table(migrationProperties.getTablePrefix() + "schema_history");

        if (migrationProperties.isAutoMigrate()) {
            configuration.load().migrate();
        }

        return configuration.load();
    }
}
