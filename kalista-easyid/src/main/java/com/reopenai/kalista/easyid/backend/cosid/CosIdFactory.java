package com.reopenai.kalista.easyid.backend.cosid;

import com.reopenai.kalista.easyid.api.IdFactory;
import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.SystemException;
import me.ahoo.cosid.IdGenerator;
import me.ahoo.cosid.provider.DefaultIdGeneratorProvider;

/**
 * Created by Allen Huang
 */
public class CosIdFactory implements IdFactory {

    static final String NAME = "cosid";

    @Override
    public long generateId() {
        return DefaultIdGeneratorProvider.INSTANCE.getShare().generate();
    }

    @Override
    public String generateStringId() {
        return DefaultIdGeneratorProvider.INSTANCE.getShare().generateAsString();
    }

    @Override
    public long generateId(String namespace) {
        return getIdGenerator(namespace).generate();
    }

    @Override
    public String generateStringId(String namespace) {
        return getIdGenerator(namespace).generateAsString();
    }

    @Override
    public String getGeneratorName() {
        return NAME;
    }

    private IdGenerator getIdGenerator(String namespace) {
        return DefaultIdGeneratorProvider.INSTANCE
                .get(namespace)
                .orElseThrow(() -> new SystemException(ErrorCode.Builtin.SERVER_ERROR, "CosId namespace " + namespace + " not found"));
    }

}
