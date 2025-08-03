package cloud.huazai.dataaccesslayer.mybatis.core.util;

import cloud.huazai.dataaccesslayer.mybatis.core.enums.DbTypeEnum;
import cloud.huazai.tool.java.lang.StringUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis 工具类
 */
public class MyBatisUtils {

    private static final String MYSQL_ESCAPE_CHARACTER = "`";

    /**
     * 将拦截器添加到链中
     * 由于 MybatisPlusInterceptor 不支持添加拦截器，所以只能全量设置
     *
     * @param interceptor 链
     * @param inner       拦截器
     * @param index       位置
     */
    public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
        List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
        inners.add(index, inner);
        interceptor.setInterceptors(inners);
    }

    /**
     * 获得 Table 对应的表名
     * <p>
     * 兼容 MySQL 转义表名 `t_xxx`
     *
     * @param table 表
     * @return 去除转移字符后的表名
     */
    public static String getTableName(Table table) {
        String tableName = table.getName();
        if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    /**
     * 构建 Column 对象
     *
     * @param tableName  表名
     * @param tableAlias 别名
     * @param column     字段名
     * @return Column 对象
     */
    public static Column buildColumn(String tableName, Alias tableAlias, String column) {
        if (tableAlias != null) {
            tableName = tableAlias.getName();
        }
        return new Column(tableName + StringPool.DOT + column);
    }

    /**
     * 跨数据库的 find_in_set 实现
     *
     * @param column 字段名称
     * @param value  查询值(不带单引号)
     * @return sql
     */
    public static String findInSet(String column, Object value) {
        DbType dbType = JdbcUtils.getDbType();
        return DbTypeEnum.getFindInSetTemplate(dbType)
                .replace("#{column}", column)
                .replace("#{value}", StringUtils.toString(value));
    }

    /**
     * 生成适用于不同数据库的 LIMIT 语句
     *
     * @param dbType 数据库类型
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 生成的 SQL 片段
     */
    public static String buildLimitSql(DbType dbType, long offset, long limit) {
        switch (dbType) {
            case ORACLE:
            case ORACLE_12C:
                // Oracle 使用 ROWNUM 或者 FETCH NEXT
                return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, limit);
            case SQL_SERVER:
            case SQL_SERVER2005:
                // SQL Server 使用 OFFSET FETCH 或者 TOP
                return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, limit);
            case POSTGRE_SQL:
            case MYSQL:
            case H2:
            case KINGBASE_ES:
            case DM:
            default:
                // MySQL、PostgreSQL、H2 等使用 LIMIT
                return String.format("LIMIT %d OFFSET %d", limit, offset);
        }
    }

    /**
     * 生成适用于不同数据库的获取当前时间的函数
     *
     * @param dbType 数据库类型
     * @return 当前时间函数
     */
    public static String getCurrentTimeFunction(DbType dbType) {
        switch (dbType) {
            case ORACLE:
            case ORACLE_12C:
                return "SYSDATE";
            case SQL_SERVER:
            case SQL_SERVER2005:
                return "GETDATE()";
            case POSTGRE_SQL:
                return "NOW()";
            case MYSQL:
                return "NOW()";
            case H2:
                return "NOW()";
            case KINGBASE_ES:
                return "NOW()";
            case DM:
                return "SYSDATE";
            default:
                return "NOW()";
        }
    }

}
