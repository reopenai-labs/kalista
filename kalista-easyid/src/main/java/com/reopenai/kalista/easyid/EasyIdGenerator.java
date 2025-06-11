package com.reopenai.kalista.easyid;

import com.reopenai.kalista.easyid.api.IdFactory;

/**
 * Created by Allen Huang
 */
public final class EasyIdGenerator {

    private static final String NAMESPACE_REQUEST_ID = System.getProperty("easy-id.namespace.request-id", "requestId");

    static IdFactory idFactory;

    public static long generateId() {
        return idFactory.generateId();
    }

    public static long generateId(String namespace) {
        return idFactory.generateId(namespace);
    }

    public static String generateStringId() {
        return idFactory.generateStringId();
    }

    public static String generateStringId(String namespace) {
        return idFactory.generateStringId(namespace);
    }

    public static String generateRequestId() {
        return idFactory.generateStringId(NAMESPACE_REQUEST_ID);
    }

}
