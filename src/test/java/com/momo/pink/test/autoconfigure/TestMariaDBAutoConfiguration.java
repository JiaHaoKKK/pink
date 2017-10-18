package com.momo.pink.test.autoconfigure;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class TestMariaDBAutoConfiguration {
    @Bean
    public MariaDB4jSpringService mariaDB4jSpringService(Environment env) {
        MariaDB4jFactoryBean mariaDB4jFactoryBean = new MariaDB4jFactoryBean();
        mariaDB4jFactoryBean.setDefaultPort(3306);
        return mariaDB4jFactoryBean;
    }
}
