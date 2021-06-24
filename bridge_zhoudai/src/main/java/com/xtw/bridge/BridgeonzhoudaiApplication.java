package com.xtw.bridge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages={"com.xtw.bridge"})
public class BridgeonzhoudaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BridgeonzhoudaiApplication.class, args);
    }

}
