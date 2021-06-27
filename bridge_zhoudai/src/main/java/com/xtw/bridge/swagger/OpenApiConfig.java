package com.xtw.bridge.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi restApi(){
        return GroupedOpenApi.builder()
                .group("user")			// 分组名
                .pathsToMatch("/users/**")    // 扫描以"/rest/"为前缀的API，分为一组

                .build();
    }

    @Bean
    public GroupedOpenApi deviceApi(){
        return GroupedOpenApi.builder()
                .group("device")					// 分组名
                .pathsToMatch("/device/**")      // 扫描以"/hello/"为前缀的API，分为一组
                .build();
    }
}
