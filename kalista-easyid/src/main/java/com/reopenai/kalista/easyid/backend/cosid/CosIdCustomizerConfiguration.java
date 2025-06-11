package com.reopenai.kalista.easyid.backend.cosid;

import com.reopenai.kalista.core.lang.reflect.XReflectUtil;
import me.ahoo.cosid.CosId;
import me.ahoo.cosid.spring.boot.starter.segment.CosIdSegmentAutoConfiguration;
import me.ahoo.cosid.spring.boot.starter.segment.CustomizeSegmentIdProperties;
import me.ahoo.cosid.spring.boot.starter.segment.SegmentIdProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Allen Huang
 */
@ConditionalOnClass(CosId.class)
@ConditionalOnBooleanProperty("cosid.enabled")
@AutoConfigureBefore(CosIdSegmentAutoConfiguration.class)
@EnableConfigurationProperties(CosIdGlobalProperties.class)
public class CosIdCustomizerConfiguration {

    @Bean
    public CosIdFactory cosIdFactory() {
        return new CosIdFactory();
    }

    @Bean
    public CustomizeSegmentIdProperties customizeSegmentIdProperties(CosIdGlobalProperties globalProperties) {
        return idProperties -> {
            // customize jdbc
            SegmentIdProperties.Distributor distributor = idProperties.getDistributor();
            if (distributor != null && SegmentIdProperties.Distributor.Type.JDBC == distributor.getType()) {
                SegmentIdProperties.Distributor.Jdbc jdbc = Optional.ofNullable(distributor.getJdbc()).orElseGet(SegmentIdProperties.Distributor.Jdbc::new);
                if (XReflectUtil.hasClass("org.postgresql.Driver")) {
                    jdbc.setInitIdSegmentSql("INSERT INTO cosid (name, last_max_id,last_fetch_time) VALUES (?, ?,floor(extract(epoch from now())));");
                    jdbc.setIncrementMaxIdSql("UPDATE cosid SET last_max_id=(last_max_id + ?),last_fetch_time=floor(extract(epoch from now())) WHERE name = ?;");
                }
                distributor.setJdbc(jdbc);
            }
            // customize providers
            Map<String, SegmentIdProperties.IdDefinition> globalProviders = Optional.ofNullable(globalProperties.getProvider()).orElse(Collections.emptyMap());
            Map<String, SegmentIdProperties.IdDefinition> provider = idProperties.getProvider();
            for (Map.Entry<String, SegmentIdProperties.IdDefinition> entry : globalProviders.entrySet()) {
                String namespace = entry.getKey();
                SegmentIdProperties.IdDefinition definition = entry.getValue();
                provider.put(namespace, definition);
            }
        };
    }

}
