package com.reopenai.kalista.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Allen Huang
 */
public final class ApplicationUtil {

    static Executor EXECUTOR = Executors.newSingleThreadExecutor();

    static Scheduler SCHEDULER = Schedulers.fromExecutor(EXECUTOR);

    static ApplicationEventPublisher publisher;

    static ApplicationContext applicationContext;

    public static Mono<Boolean> reactivePublishEvent(Object event) {
        return Mono.fromRunnable(() -> publisher.publishEvent(event))
                .publishOn(SCHEDULER)
                .thenReturn(Boolean.TRUE);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
