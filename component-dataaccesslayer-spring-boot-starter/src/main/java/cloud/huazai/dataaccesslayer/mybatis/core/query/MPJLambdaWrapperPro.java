package cloud.huazai.dataaccesslayer.mybatis.core.query;

import cloud.huazai.tool.java.lang.ArrayUtils;
import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Collection;


/**
 * 拓展 MyBatis Plus Join QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 * <p>
 * 2. SFunction<S, ?> column + <S> 泛型：支持任意类字段（主表、子表、三表），推荐写法, 让编译器自动推断 S 类型
 * @param <T> 数据类型
 */
public class MPJLambdaWrapperPro<T> extends MPJLambdaWrapper<T> {

    public <S> MPJLambdaWrapperPro<T> likeIfPresent(SFunction<S, ?> column, String val) {
        if (StringUtils.isNotBlank(val)) {
            super.like(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> inIfPresent(SFunction<S, ?> column, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> inIfPresent(SFunction<S, ?> column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            super.in(column, values);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> eqIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotEmpty(val)) {
             super.eq(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> neIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotEmpty(val)) {
            super.ne(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> gtIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.gt(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> geIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.ge(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> ltIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
            super.lt(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> leIfPresent(SFunction<S, ?> column, Object val) {
        if (ObjectUtils.isNotNull(val)) {
             super.le(column, val);
        }
        return this;
    }

    public <S> MPJLambdaWrapperPro<T> betweenIfPresent(SFunction<S, ?> column, Object... values) {
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

    public <S> MPJLambdaWrapperPro<T> betweenIfPresent(SFunction<S, ?> column, Object val1, Object val2) {
        if (ObjectUtils.isNotNull(val1) &&ObjectUtils.isNotNull(val2)) {
           super.between(column, val1, val2);
        } else if (ObjectUtils.isNotNull(val1)) {
             super.ge(column, val1);
        } else if (ObjectUtils.isNotNull(val2)) {
           super.le(column, val2);
        }
        return this;
    }

    @SafeVarargs
    public final MPJLambdaWrapperPro<T> selectIfPresent(SFunction<T, ?>... column) {
        if (ArrayUtils.isNotEmpty(column)) {
            super.select(column);
        }
        return this;
    }


    // ========== 重写父类方法，方便链式调用 ==========

    // @Override
    // public <X> MPJLambdaWrapperPro<T> eq(boolean condition, SFunction<X, ?> column, Object val) {
    //     super.eq(condition, column, val);
    //     return this;
    // }
    //
    // @Override
    // public <X> MPJLambdaWrapperPro<T> eq(SFunction<X, ?> column, Object val) {
    //     super.eq(column, val);
    //     return this;
    // }
    //
    // @Override
    // public <X> MPJLambdaWrapperPro<T> orderByDesc(SFunction<X, ?> column) {
    //     //noinspection unchecked
    //     super.orderByDesc(true, column);
    //     return this;
    // }
    //
    // @Override
    // public MPJLambdaWrapperPro<T> last(String lastSql) {
    //     super.last(lastSql);
    //     return this;
    // }
    //
    // @Override
    // public <X> MPJLambdaWrapperPro<T> in(SFunction<X, ?> column, Collection<?> coll) {
    //     super.in(column, coll);
    //     return this;
    // }
    //
    // @Override
    // public MPJLambdaWrapperPro<T> selectAll(Class<?> clazz) {
    //     super.selectAll(clazz);
    //     return this;
    // }
    //
    // @Override
    // public MPJLambdaWrapperPro<T> selectAll(Class<?> clazz, String prefix) {
    //     super.selectAll(clazz, prefix);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectAs(SFunction<S, ?> column, String alias) {
    //     super.selectAs(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <E> MPJLambdaWrapperPro<T> selectAs(String column, SFunction<E, ?> alias) {
    //     super.selectAs(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectAs(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectAs(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <E, X> MPJLambdaWrapperPro<T> selectAs(String index, SFunction<E, ?> column, SFunction<X, ?> alias) {
    //     super.selectAs(index, column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <E> MPJLambdaWrapperPro<T> selectAsClass(Class<E> source, Class<?> tag) {
    //     super.selectAsClass(source, tag);
    //     return this;
    // }
    //
    // @Override
    // public <E, F> MPJLambdaWrapperPro<T> selectSub(Class<E> clazz, Consumer<MPJLambdaWrapper<E>> consumer, SFunction<F, ?> alias) {
    //     super.selectSub(clazz, consumer, alias);
    //     return this;
    // }
    //
    // @Override
    // public <E, F> MPJLambdaWrapperPro<T> selectSub(Class<E> clazz, String st, Consumer<MPJLambdaWrapper<E>> consumer, SFunction<F, ?> alias) {
    //     super.selectSub(clazz, st, consumer, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectCount(SFunction<S, ?> column) {
    //     super.selectCount(column);
    //     return this;
    // }
    //
    // @Override
    // public MPJLambdaWrapperPro<T> selectCount(Object column, String alias) {
    //     super.selectCount(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <X> MPJLambdaWrapperPro<T> selectCount(Object column, SFunction<X, ?> alias) {
    //     super.selectCount(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectCount(SFunction<S, ?> column, String alias) {
    //     super.selectCount(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectCount(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectCount(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectSum(SFunction<S, ?> column) {
    //     super.selectSum(column);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectSum(SFunction<S, ?> column, String alias) {
    //     super.selectSum(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectSum(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectSum(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectMax(SFunction<S, ?> column) {
    //     super.selectMax(column);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectMax(SFunction<S, ?> column, String alias) {
    //     super.selectMax(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectMax(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectMax(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectMin(SFunction<S, ?> column) {
    //     super.selectMin(column);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectMin(SFunction<S, ?> column, String alias) {
    //     super.selectMin(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectMin(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectMin(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectAvg(SFunction<S, ?> column) {
    //     super.selectAvg(column);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectAvg(SFunction<S, ?> column, String alias) {
    //     super.selectAvg(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectAvg(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectAvg(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectLen(SFunction<S, ?> column) {
    //     super.selectLen(column);
    //     return this;
    // }
    //
    // @Override
    // public <S> MPJLambdaWrapperPro<T> selectLen(SFunction<S, ?> column, String alias) {
    //     super.selectLen(column, alias);
    //     return this;
    // }
    //
    // @Override
    // public <S, X> MPJLambdaWrapperPro<T> selectLen(SFunction<S, ?> column, SFunction<X, ?> alias) {
    //     super.selectLen(column, alias);
    //     return this;
    // }
    //
    // // ========== 关键重写：使 leftJoin 返回当前类型 this ==========
    // @Override
    // public <A, B> MPJLambdaWrapperPro<T> leftJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
    //     super.leftJoin(clazz, left, right);
    //     return this;
    // }
    //
    // @Override
    // public <A, B> MPJLambdaWrapperPro<T> rightJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
    //     super.rightJoin(clazz, left, right);
    //     return this;
    // }
    //
    // @Override
    // public <A, B> MPJLambdaWrapperPro<T> innerJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right) {
    //     super.innerJoin(clazz, left, right);
    //     return this;
    // }
    //
    // // ========== 添加扩展 Join 支持 ext 函数式参数 ==========
    // public <A, B> MPJLambdaWrapperPro<T> leftJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
    //     super.leftJoin(clazz, left, right);
    //     if (ext != null) ext.accept(this);
    //     return this;
    // }
    //
    // public <A, B> MPJLambdaWrapperPro<T> rightJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
    //     super.rightJoin(clazz, left, right);
    //     if (ext != null) ext.accept(this);
    //     return this;
    // }
    //
    // public <A, B> MPJLambdaWrapperPro<T> innerJoin(Class<A> clazz, SFunction<A, ?> left, SFunction<B, ?> right, Consumer<MPJLambdaWrapperX<T>> ext) {
    //     super.innerJoin(clazz, left, right);
    //     if (ext != null) ext.accept(this);
    //     return this;
    // }
}
