package cloud.huazai.dataaccesslayer.mybatis.core.query;


import cloud.huazai.dataaccesslayer.mybatis.core.util.JdbcUtils;
import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 *
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 */
public class QueryWrapperPro<T> extends QueryWrapper<T> {

    private static final String SQL_LIMIT = "LIMIT %d";
    private static final String SQL_ORACLE_ROWNUM = "ROWNUM <= %d";
    private static final String SQL_SERVER_TOP = "TOP %d";


    public QueryWrapperPro<T> likeIfPresent(String column, String val) {
        if (StringUtils.isNotBlank(val)) {
            super.like(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> inIfPresent(String column, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public QueryWrapperPro<T> inIfPresent(String column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public QueryWrapperPro<T> eqIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
           super.eq(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> neIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.ne(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> gtIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.gt(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> geIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.ge(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> ltIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
             super.lt(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> leIfPresent(String column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.le(column, val);
        }
        return this;
    }

    public QueryWrapperPro<T> betweenIfPresent(String column, Object val1, Object val2) {
        if (ObjectUtils.isNotNull(val1) && ObjectUtils.isNotNull(val2)) {
            super.between(column, val1, val2);
        } else if (ObjectUtils.isNotNull(val1)) {
            super.ge(column, val1); // 需要你实现 ge 方法
        } else if (ObjectUtils.isNotNull(val2)) {
            super.le(column, val2); // 需要你实现 le 方法
        }
        return this;
    }

    public QueryWrapperPro<T> betweenIfPresent(String column, Object... values) {
        if (values!= null && values.length != 0 && values[0] != null && values[1] != null) {
            return (QueryWrapperPro<T>) super.between(column, values[0], values[1]);
        }
        if (values!= null && values.length != 0 && values[0] != null) {
            return (QueryWrapperPro<T>) ge(column, values[0]);
        }
        if (values!= null && values.length != 0 && values[1] != null) {
            return (QueryWrapperPro<T>) le(column, values[1]);
        }
        return this;
    }

    public  QueryWrapperPro<T> selectIfPresent(String... column) {
        if (ArrayUtils.isNotEmpty(column)) {
            super.select(column);
        }
        return this;
    }



    /**
     * 限制查询结果条数（基础版）
     * @param n 限制条数，<=0 时不生效
     * @return 当前 QueryWrapperPro 实例
     */
    public QueryWrapperPro<T> limit(int n) {
        return limit(0, n);
    }

    /**
     * 分页查询（支持偏移量）
     * @param offset 偏移量（起始位置，从 0 开始）
     * @param limit  每页条数
     * @return 当前 QueryWrapperPro 实例
     */
    public QueryWrapperPro<T> limit(long offset, long limit) {
        if (limit <= 0) {
            return this;
        }

        try {
            DbType dbType = JdbcUtils.getDbType();
            if (dbType == null) {
                log.warn("无法获取数据库类型，默认使用 LIMIT 语法");
                appendLimitSql(offset, limit);
                return this;
            }

            if (JdbcUtils.isOracle(dbType)) {
                appendOracleLimitSql(offset, limit);
            } else if (JdbcUtils.isSQLServer(dbType)) {
                appendSqlServerLimitSql(offset, limit);
            } else if (JdbcUtils.isMySQL(dbType) || JdbcUtils.isPostgreSQL(dbType)
                    || JdbcUtils.isDM(dbType) || JdbcUtils.isKingbaseES(dbType)) {
                appendLimitSql(offset, limit);
            } else {
                log.warn(StringUtils.format("不支持的数据库类型：{}，默认使用 LIMIT 语法", dbType.getDesc()));
                appendLimitSql(offset, limit);
            }
        } catch (Exception e) {
            log.error("获取数据库类型或拼接分页 SQL 失败", e);
            appendLimitSql(offset, limit);
        }

        return this;
    }

    /**
     * 只返回一条记录
     *
     * @return this
     */
    public QueryWrapperPro<T> limitOne() {
        return limit(1);
    }

    /**
     * 拼接 MySQL/PostgreSQL 等通用 LIMIT 语法
     */
    private void appendLimitSql(long offset, long limit) {
        if (offset <= 0) {
            super.last(String.format(SQL_LIMIT, limit));
        } else {
            super.last(String.format("LIMIT %d OFFSET %d", limit, offset));
        }
    }

    /**
     * 拼接 Oracle 分页语法
     * <p>
     * 简单场景：直接使用 ROWNUM <= n
     * 复杂场景（带偏移量）：使用子查询 + ROWNUM 实现
     */
    private void appendOracleLimitSql(long offset, long limit) {
        if (offset <= 0) {
            super.le(SQL_ORACLE_ROWNUM, limit);
        } else {
            String sql = String.format(
                    ") T WHERE ROWNUM <= %d AND RN > %d",
                    offset + limit, offset
            );
            super.last("SELECT * FROM (SELECT TMP.*, ROWNUM RN FROM (" + sql);
        }
    }

    /**
     * 拼接 SQL Server 分页语法
     * <p>
     * 简单场景：使用 TOP n
     * 复杂场景（带偏移量）：使用 OFFSET FETCH（SQL Server 2012+）
     */
    private void appendSqlServerLimitSql(long offset, long limit) {
        if (offset <= 0) {
            super.last(String.format(SQL_SERVER_TOP, limit));
        } else {
            String sql = String.format(
                    "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY",
                    offset, limit
            );
            super.last(sql);
        }
    }

}
