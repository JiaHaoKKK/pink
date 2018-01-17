package com.momo.pink.app;

import com.momo.pink.owner.OwnerConfiguration;
import com.momo.pink.todo.TodoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Import({
    OwnerConfiguration.class,
    TodoConfiguration.class
})
@MapperScan(annotationClass = Mapper.class, value = "com.momo.pink")
public class PinkApp {

    public static void main(String[] args) {
        StandardEnvironment env = new StandardEnvironment();
        MutablePropertySources propertySources = env.getPropertySources();
        Map<String, Object> properties = new HashMap<>();
        properties.put("encrypt.key", "${encrypt.rootKey}oUzxewPh"); //naN8cE/CoUzxewPh
        propertySources.addLast(new MapPropertySource("encrypt", properties));
        new SpringApplicationBuilder(PinkApp.class)
            .properties("spring.jackson.serialization.write_dates_as_timestamps", "false")
            .environment(env)
            .build()
            .run(args);
    }
}
