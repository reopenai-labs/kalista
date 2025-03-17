package com.reopenai.kalista.r2dbc.repository;

import com.reopenai.kalista.r2dbc.condition.EasyQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Allen Huang
 */
public interface RepositoryExtension<T, ID> {

    <QUERY extends EasyQuery<QUERY, T>> Mono<T> getOne(QUERY condition);

    <QUERY extends EasyQuery<QUERY, T>> Flux<T> list(QUERY condition);

}
