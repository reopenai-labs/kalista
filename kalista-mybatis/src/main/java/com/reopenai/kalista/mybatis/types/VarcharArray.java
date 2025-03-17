package com.reopenai.kalista.mybatis.types;

import java.util.*;

/**
 * VarcharArray
 *
 * @author Allen Huang
 */
public class VarcharArray extends ArrayList<String> {

    public VarcharArray(Collection<String> elements) {
        super(elements == null ? Collections.emptyList() : elements);
    }

    public VarcharArray(int initialCapacity) {
        super(initialCapacity);
    }

    public List<String> asList() {
        return this;
    }

    public Set<String> asSet() {
        return new HashSet<>(this);
    }

    public static VarcharArray from(String[] elements) {
        VarcharArray entity = new VarcharArray(elements.length);
        for (int i = 0; i < elements.length; i++) {
            entity.add(i, elements[i]);
        }
        return entity;
    }

}
