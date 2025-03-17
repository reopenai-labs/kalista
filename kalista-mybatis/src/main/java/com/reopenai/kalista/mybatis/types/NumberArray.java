package com.reopenai.kalista.mybatis.types;

import java.util.*;
import java.util.stream.Collectors;

/**
 * NumberArray
 *
 * @author Allen Huang
 */
public class NumberArray extends ArrayList<Number> {

    public NumberArray(Collection<Number> elements) {
        super(elements == null ? Collections.emptyList() : elements);
    }

    public NumberArray(int initialCapacity) {
        super(initialCapacity);
    }

    public List<Number> asList() {
        return this;
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

    public static <T extends Number> NumberArray from(Collection<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return from(new Number[0]);
        }
        Number[] array = elements.toArray(new Number[0]);
        return from(array);
    }

    public static NumberArray from(Number[] elements) {
        if (elements == null) {
            elements = new Number[0];
        }
        NumberArray entity = new NumberArray(elements.length);
        for (int i = 0; i < elements.length; i++) {
            entity.add(i, elements[i]);
        }
        return entity;
    }

}
