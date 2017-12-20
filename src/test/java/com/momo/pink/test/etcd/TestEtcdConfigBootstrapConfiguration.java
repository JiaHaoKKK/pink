package com.momo.pink.test.etcd;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(name = "spring.cloud.test.etcd.enable", matchIfMissing = false)
public class TestEtcdConfigBootstrapConfiguration {

    @Bean
    public EtcdFactoryBean etcdFactoryBean() {
        return new EtcdFactoryBean();
    }
}
