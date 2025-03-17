package com.reopenai.kalista.mybatis.types;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen Huang
 */
@NoArgsConstructor
public class VarcharMap extends HashMap<String, String> {

    public VarcharMap(Map<String, String> elements) {
        super(elements == null ? Collections.emptyMap() : elements);
    }

    public Map<String, String> asMap() {
        return this;
    }

}
