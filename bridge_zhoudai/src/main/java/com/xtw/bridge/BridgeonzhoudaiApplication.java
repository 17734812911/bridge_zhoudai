package com.xtw.bridge;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan(basePackages={"com.xtw.bridge"})
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
public class BridgeonzhoudaiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BridgeonzhoudaiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BridgeonzhoudaiApplication.class, args);
    }

}
