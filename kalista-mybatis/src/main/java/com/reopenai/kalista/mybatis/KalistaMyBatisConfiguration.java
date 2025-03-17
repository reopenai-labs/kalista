package com.reopenai.kalista.mybatis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.ClassScanner;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.parser.cache.JdkSerialCaffeineJsqlParseCache;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.reopenai.kalista.base.enums.XEnum;
import com.reopenai.kalista.core.runtime.RuntimeUtil;
import com.reopenai.kalista.mybatis.handler.*;
import com.reopenai.kalista.mybatis.interceptor.GlobalExceptionInterceptor;
import com.reopenai.kalista.mybatis.interceptor.TransactionCheckerInterceptor;
import com.reopenai.kalista.mybatis.types.*;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Allen Huang
 */
@Configuration
public class KalistaMyBatisConfiguration implements InitializingBean {

    static {
        JsqlParserGlobal.setJsqlParseCache(new JdkSerialCaffeineJsqlParseCache(cache -> cache
                .maximumSize(512)
                .scheduler(Scheduler.systemScheduler())
                .expireAfterWrite(1, TimeUnit.MINUTES)
        ));
    }

    @Bean
    public GlobalExceptionInterceptor mybatisGlobalExceptionInterceptor() {
        return new GlobalExceptionInterceptor();
    }

    @Bean
    public TransactionCheckerInterceptor mybatisTransactionCheckerInterceptor() {
        return new TransactionCheckerInterceptor();
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return properties -> {
            // properties
            properties.setExecutorType(ExecutorType.REUSE);
            properties.setMapperLocations(new String[]{"classpath:mapper/*.xml"});
            // configuration
            MybatisPlusProperties.CoreConfiguration configuration = properties.getConfiguration();
            if (configuration == null) {
                configuration = new MybatisPlusProperties.CoreConfiguration();
                properties.setConfiguration(configuration);
            }
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
            configuration.setLogImpl(Slf4jImpl.class);
            configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
            // global-config
            GlobalConfig globalConfig = properties.getGlobalConfig();
            if (globalConfig == null) {
                globalConfig = new GlobalConfig();
                properties.setGlobalConfig(globalConfig);
            }
            globalConfig.setBanner(false);
            GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
            if (dbConfig == null) {
                dbConfig = new GlobalConfig.DbConfig();
                globalConfig.setDbConfig(dbConfig);
            }
            dbConfig.setIdType(IdType.AUTO);
            dbConfig.setLogicNotDeleteValue("floor(extract(epoch from now()))");
            dbConfig.setLogicNotDeleteValue("0");
            dbConfig.setLogicDeleteField("isDeleted");
        };
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            registerTypeHandlers(typeHandlerRegistry);
        };
    }

    private void registerTypeHandlers(TypeHandlerRegistry registry) {
        registry.register(Locale.class, LocaleTypeHandler.class);
        registry.register(JsonField.class, new JsonFieldTypeHandler());
        registry.register(VarcharMap.class, VarcharMapTypeHandler.class);
        registry.register(VarcharSet.class, VarcharSetTypeHandler.class);
        registry.register(VarcharArray.class, VarcharArrayTypeHandler.class);
        registry.register(NumberArray.class, NumberArrayTypeHandler.class);
        registry.register(NumberSet.class, NumberSetTypeHandler.class);
        // 注册XEnum相关类
        String packageName = XEnum.class.getPackageName();
        registerEnumTypeHandler(packageName, registry);
        String mainPackages = RuntimeUtil.getMainPackage();
        registerEnumTypeHandler(String.join(".", mainPackages, "enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "api.enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "base.enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "bean.enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "common.enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "message.enums"), registry);
        registerEnumTypeHandler(String.join(".", mainPackages, "api.bean.enums"), registry);
    }

    @SuppressWarnings("all")
    private void registerEnumTypeHandler(String packages, TypeHandlerRegistry registry) {
        Set<Class<?>> classes = ClassScanner.scanPackage(packages, XEnumTypeHandler::isMatch);
        if (CollUtil.isNotEmpty(classes)) {
            for (Class<?> type : classes) {
                registry.register(type, new XEnumTypeHandler(type));
                //兼容数组的格式
//                Class<?> arrayClass = Array.newInstance(type, 0).getClass();
//                registry.register(arrayClass, new XEnumArrayTypeHandler(type));
            }
        }
    }

    @Bean
    @ConditionalOnClass(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.POSTGRE_SQL);
        innerInterceptor.setMaxLimit(1024L);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<?> mainClass = RuntimeUtil.deduceMainApplicationClass();
        if (mainClass != null && mainClass.isAnnotationPresent(SpringBootApplication.class)) {
            MapperScan mapperScan = mainClass.getAnnotation(MapperScan.class);
            MapperScans mapperScans = mainClass.getAnnotation(MapperScans.class);
            if (mapperScan == null && mapperScans == null) {
                throw new BeanCreationException("必须在启动类中添加@MapperScan或@MapperScans");
            }
            if (mapperScan != null && validMapperScan(mapperScan)) {
                throw new BeanCreationException("@MapperScan中必须指定value或basePackages或basePackageClasses属性.");
            }
            if (mapperScans != null) {
                MapperScan[] scans = mapperScans.value();
                if (scans.length == 0) {
                    throw new BeanCreationException("@MapperScans中必须指定@MapperScan");
                }
                for (MapperScan scan : scans) {
                    if (validMapperScan(scan)) {
                        throw new BeanCreationException("@MapperScans中指定的@MapperScan缺少value或basePackages或basePackageClasses属性");
                    }
                }
            }
        }
    }

    private boolean validMapperScan(MapperScan mapperScan) {
        return mapperScan.value().length == 0 && mapperScan.basePackages().length == 0 && mapperScan.basePackageClasses().length == 0;
    }

}
