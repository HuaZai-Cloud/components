package cloud.huazai.dataaccesslayer.migration.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * DataMigrationHelper
 *
 * @author Devon
 * @since 2025/8/4 15:28
 */
@Slf4j
@Component
public class DataMigrationHelper {


    @Autowired
    private DataSource dataSource;

    /**
     * 执行数据迁移
     *
     * @param flyway Flyway实例
     */
    public void executeMigration(Flyway flyway) {
        MigrationInfoService infoService = flyway.info();
        MigrationInfo[] pendingMigrations = infoService.pending();

        if (pendingMigrations.length > 0) {
            log.info("Found {} pending migrations: {}", pendingMigrations.length,
                    Arrays.toString(pendingMigrations));

            MigrateResult migrationsApplied = flyway.migrate();
            log.info("Applied {} migrations", migrationsApplied);
        } else {
            log.info("No pending migrations found");
        }
    }

    /**
     * 验证迁移状态
     *
     * @param flyway Flyway实例
     */
    public void validateMigration(Flyway flyway) {
        try {
            flyway.validate();
            log.info("Migration validation passed");
        } catch (Exception e) {
            log.error("Migration validation failed", e);
            throw e;
        }
    }

    /**
     * 获取当前迁移状态信息
     *
     * @param flyway Flyway实例
     * @return 迁移信息描述
     */
    public String getMigrationStatus(Flyway flyway) {
        MigrationInfoService infoService = flyway.info();
        MigrationInfo current = infoService.current();

        if (current != null) {
            return String.format("Current migration version: %s, description: %s",
                    current.getVersion(), current.getDescription());
        } else {
            return "No migrations have been applied yet";
        }
    }
}
