package com.reopenai.kalista.easyid.backend.cosid;

import lombok.Data;
import me.ahoo.cosid.spring.boot.starter.segment.SegmentIdProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Allen Huang
 */
@Data
@ConfigurationProperties("cosid.segment.global")
public class CosIdGlobalProperties {

    private Map<String, SegmentIdProperties.IdDefinition> provider;

}
