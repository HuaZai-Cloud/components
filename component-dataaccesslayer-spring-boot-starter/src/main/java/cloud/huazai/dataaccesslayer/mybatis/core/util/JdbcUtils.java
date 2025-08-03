package cloud.huazai.dataaccesslayer.mybatis.core.util;

import cloud.huazai.dataaccesslayer.mybatis.core.enums.DbTypeEnum;
import com.baomidou.mybatisplus.annotation.DbType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC 工具类
 *
 */
public class JdbcUtils {

    /**
     * 判断连接是否正确
     *
     * @param url      数据源连接
     * @param username 账号
     * @param password 密码
     * @return 是否正确
     */
    public static boolean isConnectionOK(String url, String username, String password) {
        try (Connection ignored = DriverManager.getConnection(url, username, password)) {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获得 URL 对应的 DB 类型
     *
     * @param url URL
     * @return DB 类型
     */
    public static DbType getDbType(String url) {
        return com.baomidou.mybatisplus.extension.toolkit.JdbcUtils.getDbType(url);
    }

    /**
     * 通过当前数据库连接获得对应的 DB 类型
     *
     * @return DB 类型
     */
    public static DbType getDbType() {
        DataSource dataSource = null;
        // try {
        //     DynamicRoutingDataSource dynamicRoutingDataSource = SpringUtils.getBean(DynamicRoutingDataSource.class);
        //     dataSource = dynamicRoutingDataSource.determineDataSource();
        // } catch (NoSuchBeanDefinitionException e) {
        //     dataSource = SpringUtils.getBean(DataSource.class);
        // }

        if (dataSource == null) {
            throw new IllegalStateException("无法获取数据源");
        }

        try (Connection conn = dataSource.getConnection()) {
            return DbTypeEnum.find(conn.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取数据库类型失败: " + e.getMessage(), e);
        }
    }

    /**
     * 判断 JDBC 连接是否为 SQLServer 数据库
     *
     * @param url JDBC 连接
     * @return 是否为 SQLServer 数据库
     */
    public static boolean isSQLServer(String url) {
        DbType dbType = getDbType(url);
        return isSQLServer(dbType);
    }

    /**
     * 判断 JDBC 连接是否为 SQLServer 数据库
     *
     * @param dbType DB 类型
     * @return 是否为 SQLServer 数据库
     */
    public static boolean isSQLServer(DbType dbType) {
        return dbType == DbType.SQL_SERVER || dbType == DbType.SQL_SERVER2005;
    }

    /**
     * 判断是否为 Oracle 数据库
     *
     * @param dbType DB 类型
     * @return 是否为 Oracle 数据库
     */
    public static boolean isOracle(DbType dbType) {
        return dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C;
    }

    /**
     * 判断是否为 MySQL 数据库
     *
     * @param dbType DB 类型
     * @return 是否为 MySQL 数据库
     */
    public static boolean isMySQL(DbType dbType) {
        return dbType == DbType.MYSQL;
    }

    /**
     * 判断是否为 PostgreSQL 数据库
     *
     * @param dbType DB 类型
     * @return 是否为 PostgreSQL 数据库
     */
    public static boolean isPostgreSQL(DbType dbType) {
        return dbType == DbType.POSTGRE_SQL;
    }

}
