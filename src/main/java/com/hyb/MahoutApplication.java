package com.hyb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hyb.mapper")
@SpringBootApplication
public class MahoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(MahoutApplication.class, args);
    }

}
