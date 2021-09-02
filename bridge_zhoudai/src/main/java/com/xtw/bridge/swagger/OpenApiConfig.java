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
                .group("users")			// 分组名
                .pathsToMatch("/users/**")    // 扫描以"/rest/"为前缀的API，分为一组
                .build();
    }

    @Bean
    public GroupedOpenApi deviceApi(){
        return GroupedOpenApi.builder()
                .group("device")					// 分组名
                .pathsToMatch("/device/**")      // 扫描以"/device/"为前缀的API，分为一组
                .build();
    }

    @Bean
    public GroupedOpenApi alertDeviceApi(){
        return GroupedOpenApi.builder()
                .group("alert")					// 分组名
                .pathsToMatch("/alert/**")      // 扫描以"/alert/"为前缀的API，分为一组
                .build();
    }

    @Bean
    public GroupedOpenApi deviceConfigApi(){
        return GroupedOpenApi.builder()
                .group("configs")					// 分组名
                .pathsToMatch("/config/**")      // 扫描以"/alert/"为前缀的API，分为一组
                .build();
    }

    @Bean
    public GroupedOpenApi cameraApi(){
        return GroupedOpenApi.builder()
                .group("camera")					// 分组名
                .pathsToMatch("/camera/**")      // 扫描以"/camera/"为前缀的API，分为一组
                .build();
    }
}
