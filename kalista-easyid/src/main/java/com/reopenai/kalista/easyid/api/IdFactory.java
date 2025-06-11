package com.reopenai.kalista.easyid.api;

/**
 * Created by Allen Huang
 */
public interface IdFactory {

    long generateId();

    long generateId(String namespace);

    String generateStringId();

    String generateStringId(String namespace);

    String getGeneratorName();

}
