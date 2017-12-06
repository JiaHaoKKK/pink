package com.momo.pink.app;

import com.momo.pink.owner.OwnerConfiguration;
import com.momo.pink.todo.TodoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    OwnerConfiguration.class,
    TodoConfiguration.class
})
@MapperScan(annotationClass = Mapper.class, value = "com.momo.pink")
public class PinkApp {
    public static void main(String[] args) {
        System.setProperty("spring.jackson.serialization.write_dates_as_timestamps", "false");
        SpringApplication.run(PinkApp.class, args);
    }
}
