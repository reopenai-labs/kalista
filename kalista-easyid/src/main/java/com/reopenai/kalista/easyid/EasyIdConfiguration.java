package com.reopenai.kalista.easyid;

import com.reopenai.kalista.easyid.backend.cosid.CosIdCustomizerConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Allen Huang
 */
@Configuration
@ImportAutoConfiguration(CosIdCustomizerConfiguration.class)
public class EasyIdConfiguration {

}
