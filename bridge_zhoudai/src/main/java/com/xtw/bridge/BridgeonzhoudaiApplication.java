package com.xtw.bridge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@MapperScan(basePackages={"com.xtw.bridge"})
public class BridgeonzhoudaiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BridgeonzhoudaiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BridgeonzhoudaiApplication.class, args);
    }

}
