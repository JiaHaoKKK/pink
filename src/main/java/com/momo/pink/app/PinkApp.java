package com.momo.pink.app;

import com.momo.pink.ns.NSConfiguration;
import com.momo.pink.todo.TodoConfiguration;
import com.momo.pink.user.UserConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    UserConfiguration.class,
    NSConfiguration.class,
    TodoConfiguration.class
})
@MapperScan("com.momo.pink")
public class PinkApp {
    public static void main(String[] args) {
        SpringApplication.run(PinkApp.class, args);
    }
}