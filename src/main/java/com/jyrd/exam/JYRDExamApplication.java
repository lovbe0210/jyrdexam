package com.jyrd.exam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jyrd.exam.base.mapper")
public class JYRDExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(JYRDExamApplication.class, args);
    }

}
