package com.reopenai.kalista.r2dbc.condition;

import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * Created by Allen Huang
 */
@SuppressWarnings("all")
public abstract class AbstractEasyQuery<T, CHILDREN extends EasyQuery<CHILDREN, T>> implements EasyQuery<CHILDREN, T> {

    protected final Class<T> entityType;

    protected Criteria criteria = Criteria.empty();

    protected List<String> selectColumns = new ArrayList<>();

    protected List<Sort.Order> orders = new ArrayList<>();

    protected AbstractEasyQuery(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public CHILDREN select(String... columns) {
        for (String column : columns) {
            selectColumns.add(column);
        }
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN eq(String column, Object value) {
        this.criteria = this.criteria.and(where(column).is(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN not(String column, Object value) {
        this.criteria = this.criteria.and(where(column).not(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN in(String column, Object... values) {
        this.criteria = this.criteria.and(where(column).in(values));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN in(String column, Collection<?> values) {
        this.criteria = this.criteria.and(where(column).in(values));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN notIn(String column, Object... values) {
        this.criteria = this.criteria.and(where(column).notIn(values));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN notIn(String column, Collection<?> values) {
        this.criteria = this.criteria.and(where(column).notIn(values));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN between(String column, Object begin, Object end) {
        this.criteria = this.criteria.and(where(column).between(begin, end));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN notBetween(String column, Object begin, Object end) {
        this.criteria = this.criteria.and(where(column).notBetween(begin, end));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN lt(String column, Object value) {
        this.criteria = this.criteria.and(where(column).lessThan(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN le(String column, Object value) {
        this.criteria = this.criteria.and(where(column).lessThanOrEquals(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN gt(String column, Object value) {
        this.criteria = this.criteria.and(where(column).greaterThan(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN ge(String column, Object value) {
        this.criteria = this.criteria.and(where(column).greaterThanOrEquals(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN like(String column, Object value) {
        this.criteria = this.criteria.and(where(column).like(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN notLike(String column, Object value) {
        this.criteria = this.criteria.and(where(column).notLike(value));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN isNull(String column) {
        this.criteria = this.criteria.and(where(column).isNull());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN isNotNull(String column) {
        this.criteria = this.criteria.and(where(column).isNotNull());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN isTrue(String column) {
        this.criteria = this.criteria.and(where(column).isTrue());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN isFalse(String column) {
        this.criteria = this.criteria.and(where(column).isFalse());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN and(Function<CHILDREN, CHILDREN> func) {
        CHILDREN ref = func.apply(newInstance());
        this.criteria = this.criteria.and(ref.asCriteria());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN or(Function<CHILDREN, CHILDREN> func) {
        CHILDREN ref = func.apply(newInstance());
        this.criteria = this.criteria.or(ref.asCriteria());
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN orderByAsc(String column) {
        this.orders.add(Sort.Order.asc(column));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN orderByAsc(List<String> columns) {
        for (String column : columns) {
            this.orders.add(Sort.Order.asc(column));
        }
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN orderByDesc(String column) {
        this.orders.add(Sort.Order.desc(column));
        return (CHILDREN) this;
    }

    @Override
    public CHILDREN orderByDesc(List<String> columns) {
        for (String column : columns) {
            this.orders.add(Sort.Order.desc(column));
        }
        return (CHILDREN) this;
    }

    @Override
    public Criteria asCriteria() {
        return this.criteria;
    }

    @Override
    public Query asQuery() {
        Query query = Query.query(this.criteria);
        if (this.orders.size() > 0) {
            query = query.sort(Sort.by(this.orders));
        }
        if (this.selectColumns.size() > 0) {
            query = query.columns(this.selectColumns);
        }
        return query;
    }

    @Override
    public Class<T> getEntityType() {
        return entityType;
    }

    abstract CHILDREN newInstance();
}
