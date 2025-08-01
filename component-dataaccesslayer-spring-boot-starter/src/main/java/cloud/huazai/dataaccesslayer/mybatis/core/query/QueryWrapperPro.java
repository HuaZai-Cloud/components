package cloud.huazai.dataaccesslayer.mybatis.core.query;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 *
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 */
public class QueryWrapperPro<T> extends QueryWrapper<T> {

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

    // ========== 重写父类方法，方便链式调用 ==========

    // @Override
    // public QueryWrapperPro<T> eq(boolean condition, String column, Object val) {
    //     super.eq(condition, column, val);
    //     return this;
    // }
    //
    // @Override
    // public QueryWrapperPro<T> eq(String column, Object val) {
    //     super.eq(column, val);
    //     return this;
    // }
    //
    // @Override
    // public QueryWrapperPro<T> orderByDesc(String column) {
    //     super.orderByDesc(true, column);
    //     return this;
    // }
    //
    // @Override
    // public QueryWrapperPro<T> last(String lastSql) {
    //     super.last(lastSql);
    //     return this;
    // }
    //
    // @Override
    // public QueryWrapperPro<T> in(String column, Collection<?> coll) {
    //     super.in(column, coll);
    //     return this;
    // }

    // /**
    //  * 设置只返回最后一条
    //  *
    //  * TODO 不是完美解，需要在思考下。如果使用多数据源，并且数据源是多种类型时，可能会存在问题：实现之返回一条的语法不同
    //  *
    //  * @return this
    //  */
    // public QueryWrapperPro<T> limitN(int n) {
    //     DbType dbType = JdbcUtils.getDbType();
    //     switch (dbType) {
    //         case ORACLE:
    //         case ORACLE_12C:
    //             super.le("ROWNUM", n);
    //             break;
    //         case SQL_SERVER:
    //         case SQL_SERVER2005:
    //             super.select("TOP " + n + " *"); // 由于 SQL Server 是通过 SELECT TOP 1 实现限制一条，所以只好使用 * 查询剩余字段
    //             break;
    //         default: // MySQL、PostgreSQL、DM 达梦、KingbaseES 大金都是采用 LIMIT 实现
    //             super.last("LIMIT " + n);
    //     }
    //     return this;
    // }

}
