package cloud.huazai.dataaccesslayer.mybatis.core.query;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 */
public class LambdaQueryWrapperPro<T> extends LambdaQueryWrapper<T> {

    private SFunction<T, ?>[] column;

    public LambdaQueryWrapperPro<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.isNotBlank(val)) {
            super.like(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.eq(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.ne(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.gt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.ge(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.lt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.le(column, val);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (ObjectUtils.isNotNull(val1) && ObjectUtils.isNotNull(val2)) {
            super.between(column, val1, val2);
        } else if (ObjectUtils.isNotNull(val1)) {
            ge(column, val1);
        } else if (ObjectUtils.isNotNull(val2)) {
            le(column, val2);
        }
        return this;
    }

    public LambdaQueryWrapperPro<T> betweenIfPresent(SFunction<T, ?> column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            switch (values.length) {
                case 2:
                    // Check for null values
                    if (values[0] != null && values[1] != null) {
                        super.between(column, values[0], values[1]);
                    } else {
                        throw new IllegalArgumentException("Both values for 'between' must not be null.");
                    }
                    break;
                case 1:
                    // Apply greater than or equal if only one value is provided
                    if (values[0] != null) {
                        super.ge(column, values[0]);
                    } else {
                        throw new IllegalArgumentException("Value for 'ge' must not be null.");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Between condition supports either one or two parameters only.");
            }
        }
        return this;
    }


    @SafeVarargs
    public final LambdaQueryWrapperPro<T> selectIfPresent(SFunction<T, ?>... column) {
        if (ArrayUtils.isNotEmpty(column)) {
            super.select(column);
        }
        return this;
    }



    // ========== 重写父类方法，方便链式调用 ==========

    // @Override
    // public LambdaQueryWrapperPro<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
    //     super.eq(condition, column, val);
    //     return this;
    // }
    //
    // @Override
    // public LambdaQueryWrapperPro<T> eq(SFunction<T, ?> column, Object val) {
    //     super.eq(column, val);
    //     return this;
    // }
    //
    // @Override
    // public LambdaQueryWrapperPro<T> orderByDesc(SFunction<T, ?> column) {
    //     super.orderByDesc(true, column);
    //     return this;
    // }
    //
    // @Override
    // public LambdaQueryWrapperPro<T> last(String lastSql) {
    //     super.last(lastSql);
    //     return this;
    // }
    //
    // @Override
    // public LambdaQueryWrapperPro<T> in(SFunction<T, ?> column, Collection<?> coll) {
    //     super.in(column, coll);
    //     return this;
    // }

}
