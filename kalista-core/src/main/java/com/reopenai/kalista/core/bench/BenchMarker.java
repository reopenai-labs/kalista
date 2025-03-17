package com.reopenai.kalista.core.bench;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Created by Allen Huang
 */
public class BenchMarker {

    private static final Map<Method, String> METHOD_FLAG_MAP = new HashMap<>();

    private static final Lock METHOD_FLAG_LOCK = new ReentrantLock(false);

    private final StringBuilder builder = new StringBuilder();

    private final long startTime;

    private long endTime;

    private String result;

    public BenchMarker() {
        this(System.currentTimeMillis());
    }

    public BenchMarker(long startTime) {
        this.startTime = startTime;
        this.endTime = startTime;
    }

    public void mark(String flag) {
        long currentTime = System.currentTimeMillis();
        builder.append(" ")
                .append(flag)
                .append(":").append(currentTime - endTime)
                .append(":").append(currentTime - startTime);
        endTime = currentTime;
    }

    public String getResult() {
        if (result == null) {
            long currentTime = System.currentTimeMillis();
            result = builder.append(" total=").append(currentTime - startTime).append("ms").toString();
        }
        return result;
    }

    public static String parseMethodFlag(Method method) {
        String flag = METHOD_FLAG_MAP.get(method);
        if (flag == null) {
            METHOD_FLAG_LOCK.lock();
            try {
                flag = METHOD_FLAG_MAP.get(method);
                if (flag == null) {
                    String simpleClassName = method.getDeclaringClass().getSimpleName();
                    StringBuilder initials = new StringBuilder();
                    // 遍历类名的每个字符
                    for (char c : simpleClassName.toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            initials.append(c);
                        }
                    }
                    String methodName = method.getName();
                    char[] charArray = methodName.toCharArray();
                    initials.append(".").append(charArray[0]);
                    for (int i = 1; i < charArray.length; i++) {
                        char c = charArray[i];
                        if (Character.isUpperCase(c)) {
                            initials.append(c);
                        }
                    }
                    flag = initials.toString();
                    METHOD_FLAG_MAP.put(method, flag);
                }
            } finally {
                METHOD_FLAG_LOCK.unlock();
            }
        }
        return flag;
    }

    public static <T> Flux<T> markWithContext(Flux<T> publisher, ContextView ctx, Supplier<String> flagSupplier) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            String flag = flagSupplier.get();
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Mono<T> markWithContext(Mono<T> publisher, ContextView ctx, Supplier<String> flagSupplier) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            String flag = flagSupplier.get();
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Flux<T> markWithContext(Flux<T> publisher, ContextView ctx, String flag) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Mono<T> markWithContext(Mono<T> publisher, ContextView ctx, String flag) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    private static <T> Flux<T> doWithContext(Flux<T> publisher, BenchMarker benchMarker, String flag) {
        return publisher
                .doOnRequest(event -> benchMarker.mark(flag))
                .doOnTerminate(() -> benchMarker.mark(flag));
    }

    private static <T> Mono<T> doWithContext(Mono<T> publisher, BenchMarker benchMarker, String flag) {
        return publisher
                .doOnRequest(event -> benchMarker.mark(flag))
                .doOnTerminate(() -> benchMarker.mark(flag));
    }

}
