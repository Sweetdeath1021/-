package org.grade;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"org.grade.mapper"})
public class IntelGradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntelGradingApplication.class, args);
    }
}
