package com.reopenai.kalista.mybatis.types;

import java.util.*;

/**
 * VarcharSet
 *
 * @author Allen Huang
 */
public class VarcharSet extends HashSet<String> {

    public VarcharSet(Collection<String> elements) {
        super(elements == null ? Collections.emptySet() : elements);
    }

    public VarcharSet(int initialCapacity) {
        super(initialCapacity);
    }

    public List<String> asList() {
        return new ArrayList<>(this);
    }

    public Set<String> asSet() {
        return this;
    }

    public static VarcharSet from(String[] elements) {
        VarcharSet entity = new VarcharSet(elements.length);
        for (String element : elements) {
            entity.add(element);
        }
        return entity;
    }

}
