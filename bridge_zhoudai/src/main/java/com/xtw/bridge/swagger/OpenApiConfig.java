package com.xtw.bridge.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // 假如有两个controller,PersionController中的API都是以"/rest/"为前缀
    // HelloController中的API都是以"/hello/"为前缀
    // 启动项目，访问 http://ip:port/swagger-ui.html

    @Bean
    public GroupedOpenApi restApi(){
       return GroupedOpenApi.builder()
               .group("rest-api")
               .pathsToMatch("/rest/**")    // 扫描以"/rest/"为前缀的API，分为一组
               .build();
    }

    @Bean
    public GroupedOpenApi helloApt(){
        return GroupedOpenApi.builder()
                .group("hello")
                .pathsToMatch("/hello/**")      // 扫描以"/hello/"为前缀的API，分为一组
                .build();
    }
}
