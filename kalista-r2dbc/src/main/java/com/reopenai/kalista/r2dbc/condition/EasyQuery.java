package com.reopenai.kalista.r2dbc.condition;

import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Allen Huang
 */
@SuppressWarnings("unchecked")
public interface EasyQuery<CHILDREN, T> {

    CHILDREN select(String... column);

    /**
     * Creates a {@link Criteria} using equality.
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN eq(String column, Object value);

    default CHILDREN eq(boolean condition, String column, Object value) {
        if (condition) {
            return eq(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using equality (is not).
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN not(String column, Object value);

    default CHILDREN not(boolean condition, String column, Object value) {
        if (condition) {
            return not(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IN}.
     *
     * @param values must not be {@literal null}.
     */
    CHILDREN in(String column, Object... values);

    default CHILDREN in(boolean condition, String column, Object... values) {
        if (condition) {
            return in(column, values);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IN}.
     *
     * @param values must not be {@literal null}.
     */
    CHILDREN in(String column, Collection<?> values);

    default CHILDREN in(boolean condition, String column, Collection<?> values) {
        if (condition) {
            return in(column, values);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT IN}.
     *
     * @param values must not be {@literal null}.
     */
    CHILDREN notIn(String column, Object... values);

    default CHILDREN notIn(boolean condition, String column, Object... values) {
        if (condition) {
            return notIn(column, values);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT IN}.
     *
     * @param values must not be {@literal null}.
     */
    CHILDREN notIn(String column, Collection<?> values);

    default CHILDREN notIn(boolean condition, String column, Collection<?> values) {
        if (condition) {
            return notIn(column, values);
        }
        return (CHILDREN) this;
    }


    /**
     * Creates a {@link Criteria} using between ({@literal BETWEEN begin AND end}).
     *
     * @param begin must not be {@literal null}.
     * @param end   must not be {@literal null}.
     * @since 2.2
     */
    CHILDREN between(String column, Object begin, Object end);

    default CHILDREN between(boolean condition, String column, Object begin, Object end) {
        if (condition) {
            return between(column, begin, end);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using not between ({@literal NOT BETWEEN begin AND end}).
     *
     * @param begin must not be {@literal null}.
     * @param end   must not be {@literal null}.
     * @since 2.2
     */
    CHILDREN notBetween(String column, Object begin, Object end);

    default CHILDREN notBetween(boolean condition, String column, Object begin, Object end) {
        if (condition) {
            return notBetween(column, begin, end);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using less-than ({@literal <}).
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN lt(String column, Object value);

    default CHILDREN lt(boolean condition, String column, Object value) {
        if (condition) {
            return lt(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using less-than or equal to ({@literal <=}).
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN le(String column, Object value);

    default CHILDREN le(boolean condition, String column, Object value) {
        if (condition) {
            return le(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using greater-than({@literal >}).
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN gt(String column, Object value);

    default CHILDREN gt(boolean condition, String column, Object value) {
        if (condition) {
            return gt(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using greater-than or equal to ({@literal >=}).
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN ge(String column, Object value);

    default CHILDREN ge(boolean condition, String column, Object value) {
        if (condition) {
            return ge(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code LIKE}.
     *
     * @param value must not be {@literal null}.
     */
    CHILDREN like(String column, Object value);

    default CHILDREN like(boolean condition, String column, Object value) {
        if (condition) {
            return like(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code NOT LIKE}.
     *
     * @param value must not be {@literal null}
     * @return a new {@link Criteria} object
     */
    CHILDREN notLike(String column, Object value);

    default CHILDREN notLike(boolean condition, String column, Object value) {
        if (condition) {
            return notLike(column, value);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS NULL}.
     */
    CHILDREN isNull(String column);

    default CHILDREN isNull(boolean condition, String column) {
        if (condition) {
            return isNull(column);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS NOT NULL}.
     */
    CHILDREN isNotNull(String column);

    default CHILDREN isNotNull(boolean condition, String column) {
        if (condition) {
            return isNotNull(column);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS TRUE}.
     *
     * @return a new {@link Criteria} object
     */
    CHILDREN isTrue(String column);

    default CHILDREN isTrue(boolean condition, String column) {
        if (condition) {
            return isTrue(column);
        }
        return (CHILDREN) this;
    }

    /**
     * Creates a {@link Criteria} using {@code IS FALSE}.
     *
     * @return a new {@link Criteria} object
     */
    CHILDREN isFalse(String column);

    default CHILDREN isFalse(boolean condition, String column) {
        if (condition) {
            return isFalse(column);
        }
        return (CHILDREN) this;
    }

    CHILDREN and(Function<CHILDREN, CHILDREN> func);

    default CHILDREN and(boolean condition, Function<CHILDREN, CHILDREN> func) {
        if (condition) {
            return and(func);
        }
        return (CHILDREN) this;
    }

    CHILDREN or(Function<CHILDREN, CHILDREN> func);

    default CHILDREN or(boolean condition, Function<CHILDREN, CHILDREN> func) {
        if (condition) {
            return or(func);
        }
        return (CHILDREN) this;
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return CHILDREN
     */
    default CHILDREN orderByAsc(boolean condition, String column) {
        if (condition) {
            return orderByAsc(column);
        }
        return (CHILDREN) this;
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc("id")</p>
     *
     * @param column 单个字段
     * @return CHILDREN
     */
    CHILDREN orderByAsc(String column);

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return CHILDREN
     */
    default CHILDREN orderByAsc(boolean condition, List<String> columns) {
        if (condition) {
            return orderByAsc(columns);
        }
        return (CHILDREN) this;
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(Arrays.asList("id", "name"))</p>
     *
     * @param columns 字段数组
     * @return CHILDREN
     */
    CHILDREN orderByAsc(List<String> columns);

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return CHILDREN
     */
    default CHILDREN orderByDesc(boolean condition, String column) {
        if (condition) {
            return orderByDesc(column);
        }
        return (CHILDREN) this;
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc("id")</p>
     *
     * @param column 字段
     * @return CHILDREN
     */
    CHILDREN orderByDesc(String column);

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段列表
     * @return CHILDREN
     */
    default CHILDREN orderByDesc(boolean condition, List<String> columns) {
        if (condition) {
            return orderByDesc(columns);
        }
        return (CHILDREN) this;
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     *
     * @param columns 字段列表
     */
    CHILDREN orderByDesc(List<String> columns);

    Criteria asCriteria();

    Query asQuery();

    Class<T> getEntityType();

}
