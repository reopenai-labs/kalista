package com.reopenai.kalista.r2dbc.condition;

import com.reopenai.kalista.core.lang.lambda.XFunction;
import com.reopenai.kalista.core.lang.lambda.XLambdaUtil;
import org.springframework.data.relational.core.query.Criteria;

import java.util.Collection;

/**
 * Created by Allen Huang
 */
public class LambdaEaseQuery<T> extends AbstractEasyQuery<T, LambdaEaseQuery<T>> {

    public LambdaEaseQuery(Class<T> entityType) {
        super(entityType);
    }

    /**
     * Creates a {@link Criteria} using equality.
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> eq(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return eq(s, value);
    }

    public LambdaEaseQuery<T> eq(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return eq(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using equality (is not).
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> not(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return not(s, value);
    }

    public LambdaEaseQuery<T> not(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return not(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IN}.
     *
     * @param values must not be {@literal null}.
     */
    public LambdaEaseQuery<T> in(XFunction<T, ?> column, Object... values) {
        String s = XLambdaUtil.property(column);
        return in(s, values);
    }

    public LambdaEaseQuery<T> in(boolean condition, XFunction<T, ?> column, Object... values) {
        if (condition) {
            return in(column, values);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IN}.
     *
     * @param values must not be {@literal null}.
     */
    public LambdaEaseQuery<T> in(XFunction<T, ?> column, Collection<?> values) {
        String s = XLambdaUtil.property(column);
        return in(s, values);
    }

    public LambdaEaseQuery<T> in(boolean condition, XFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            return in(column, values);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT IN}.
     *
     * @param values must not be {@literal null}.
     */
    public LambdaEaseQuery<T> notIn(XFunction<T, ?> column, Object... values) {
        String s = XLambdaUtil.property(column);
        return notIn(s, values);
    }

    public LambdaEaseQuery<T> notIn(boolean condition, XFunction<T, ?> column, Object... values) {
        if (condition) {
            return notIn(column, values);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT IN}.
     *
     * @param values must not be {@literal null}.
     */
    public LambdaEaseQuery<T> notIn(XFunction<T, ?> column, Collection<?> values) {
        String s = XLambdaUtil.property(column);
        return notIn(s, values);
    }

    public LambdaEaseQuery<T> notIn(boolean condition, XFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            return notIn(column, values);
        }
        return this;
    }


    /**
     * Creates a {@link Criteria} using between ({@literal BETWEEN begin AND end}).
     *
     * @param begin must not be {@literal null}.
     * @param end   must not be {@literal null}.
     * @since 2.2
     */
    public LambdaEaseQuery<T> between(XFunction<T, ?> column, Object begin, Object end) {
        String s = XLambdaUtil.property(column);
        return between(s, begin, end);
    }

    public LambdaEaseQuery<T> between(boolean condition, XFunction<T, ?> column, Object begin, Object end) {
        if (condition) {
            return between(column, begin, end);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using not between ({@literal NOT BETWEEN begin AND end}).
     *
     * @param begin must not be {@literal null}.
     * @param end   must not be {@literal null}.
     * @since 2.2
     */
    public LambdaEaseQuery<T> notBetween(XFunction<T, ?> column, Object begin, Object end) {
        String s = XLambdaUtil.property(column);
        return notBetween(s, begin, end);
    }

    public LambdaEaseQuery<T> notBetween(boolean condition, XFunction<T, ?> column, Object begin, Object end) {
        if (condition) {
            return notBetween(column, begin, end);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using less-than ({@literal <}).
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> lt(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return lt(s, value);
    }

    public LambdaEaseQuery<T> lt(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return lt(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using less-than or equal to ({@literal <=}).
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> le(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return le(s, value);
    }

    public LambdaEaseQuery<T> le(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return le(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using greater-than({@literal >}).
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> gt(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return gt(s, value);
    }

    public LambdaEaseQuery<T> gt(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return gt(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using greater-than or equal to ({@literal >=}).
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> ge(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return ge(s, value);
    }

    public LambdaEaseQuery<T> ge(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return ge(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code LIKE}.
     *
     * @param value must not be {@literal null}.
     */
    public LambdaEaseQuery<T> like(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return like(s, value);
    }

    public LambdaEaseQuery<T> like(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return like(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT LIKE}.
     *
     * @param value must not be {@literal null}
     * @return a new {@link Criteria} object
     */
    public LambdaEaseQuery<T> notLike(XFunction<T, ?> column, Object value) {
        String s = XLambdaUtil.property(column);
        return notLike(s, value);
    }

    public LambdaEaseQuery<T> notLike(boolean condition, XFunction<T, ?> column, Object value) {
        if (condition) {
            return notLike(column, value);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS NULL}.
     */
    public LambdaEaseQuery<T> isNull(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return isNull(s);
    }

    public LambdaEaseQuery<T> isNull(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return isNull(column);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS NOT NULL}.
     */
    public LambdaEaseQuery<T> isNotNull(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return isNotNull(s);
    }

    public LambdaEaseQuery<T> isNotNull(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return isNotNull(column);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS TRUE}.
     *
     * @return a new {@link Criteria} object
     */
    public LambdaEaseQuery<T> isTrue(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return isTrue(s);
    }

    public LambdaEaseQuery<T> isTrue(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return isTrue(column);
        }
        return this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS FALSE}.
     *
     * @return a new {@link Criteria} object
     */
    public LambdaEaseQuery<T> isFalse(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return isFalse(s);
    }

    public LambdaEaseQuery<T> isFalse(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return isFalse(column);
        }
        return this;
    }


    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return LambdaEaseQuery<T>
     */
    public LambdaEaseQuery<T> orderByAsc(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return orderByAsc(column);
        }
        return this;
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc("id")</p>
     *
     * @param column 单个字段
     * @return LambdaEaseQuery<T>
     */
    public LambdaEaseQuery<T> orderByAsc(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return orderByAsc(s);
    }


    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return LambdaEaseQuery<T>
     */
    public LambdaEaseQuery<T> orderByDesc(boolean condition, XFunction<T, ?> column) {
        if (condition) {
            return orderByDesc(column);
        }
        return this;
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc("id")</p>
     *
     * @param column 字段
     * @return LambdaEaseQuery<T>
     */
    public LambdaEaseQuery<T> orderByDesc(XFunction<T, ?> column) {
        String s = XLambdaUtil.property(column);
        return orderByDesc(s);
    }

    @Override
    LambdaEaseQuery<T> newInstance() {
        return new LambdaEaseQuery<>(this.entityType);
    }

}
