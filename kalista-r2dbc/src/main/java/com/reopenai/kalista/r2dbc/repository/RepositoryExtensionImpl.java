package com.reopenai.kalista.r2dbc.repository;

import com.reopenai.kalista.core.bench.BenchMakerIgnore;
import com.reopenai.kalista.r2dbc.condition.EasyQuery;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Allen Huang
 */
@Component
@BenchMakerIgnore
public class RepositoryExtensionImpl<T, ID> implements RepositoryExtension<T, ID> {

    private final R2dbcEntityTemplate template;

    public RepositoryExtensionImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }


    @Override
    public <QUERY extends EasyQuery<QUERY, T>> Mono<T> getOne(QUERY condition) {
        Query query = condition.asQuery();
        return template.selectOne(query, condition.getEntityType());
    }

    @Override
    public <QUERY extends EasyQuery<QUERY, T>> Flux<T> list(QUERY condition) {
        Query query = condition.asQuery();
        return template.select(query, condition.getEntityType());
    }

}
