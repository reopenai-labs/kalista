package com.reopenai.kalista.mybatis.types;

import java.util.*;
import java.util.stream.Collectors;

/**
 * NumberArray
 *
 * @author Allen Huang
 */
public class NumberSet extends HashSet<Number> {

    public NumberSet(Collection<Number> elements) {
        super(elements == null ? Collections.emptySet() : elements);
    }

    public NumberSet(int initialCapacity) {
        super(initialCapacity);
    }

    public List<Number> asList() {
        return new ArrayList<>(this);
    }

    public Set<Number> asSet() {
        return new HashSet<>(this);
    }

    public List<Long> asLongList() {
        return this.stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
    }

    public Set<Long> asLongSet() {
        return this.stream()
                .map(Number::longValue)
                .collect(Collectors.toSet());
    }

    public List<Integer> asIntegerList() {
        return this.stream()
                .map(Number::intValue)
                .collect(Collectors.toList());
    }

    public Set<Integer> asIntegerSet() {
        return this.stream()
                .map(Number::intValue)
                .collect(Collectors.toSet());
    }

    public static <T extends Number> NumberSet from(Collection<T> elements) {
        Number[] array = elements.toArray(new Number[0]);
        return from(array);
    }

    public static NumberSet from(Number[] elements) {
        NumberSet entity = new NumberSet(elements.length);
        entity.addAll(Set.of(elements));
        return entity;
    }

}
