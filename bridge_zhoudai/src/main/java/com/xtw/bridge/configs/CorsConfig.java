package com.xtw.bridge.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * User: Mr.Chen
 * Date: 2021/8/9
 * Description: No Description
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //是否允许请求带有验证信息
        corsConfiguration.setAllowCredentials(true);
        /*允许服务端访问的客户端请求头*/
        corsConfiguration.addAllowedHeader("*");
        //1,允许任何来源
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        //2,允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        //3,允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        //4,允许凭证
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}


// @Component
// public class CorsConfig implements Filter {
//
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//         HttpServletRequest request = (HttpServletRequest) servletRequest;
//         HttpServletResponse resp = (HttpServletResponse) servletResponse;
//
//         // 告诉浏览器允许所有的域访问
//         // 注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
//         // resp.addHeader("Access-Control-Allow-Origin", "*");
//         // 解决办法通过获取Origin请求头来动态设置
//         String origin = request.getHeader("Origin");
//         if (StringUtils.hasText(origin)) {
//             resp.addHeader("Access-Control-Allow-Origin", origin);
//         }
//         // 允许带有cookie访问
//         resp.addHeader("Access-Control-Allow-Credentials", "true");
//
//         // 告诉浏览器允许跨域访问的方法
//         resp.addHeader("Access-Control-Allow-Methods", "*");
//
//         // 告诉浏览器允许带有Content-Type,header1,header2头的请求访问
//         // resp.addHeader("Access-Control-Allow-Headers", "Content-Type,header1,header2");
//         // 设置支持所有的自定义请求头
//         String headers = request.getHeader("Access-Control-Request-Headers");
//         if (StringUtils.hasText(headers)) {
//             resp.addHeader("Access-Control-Allow-Headers", headers);
//         }
//
//         // 告诉浏览器缓存OPTIONS预检请求1小时,避免非简单请求每次发送预检请求,提升性能
//         resp.addHeader("Access-Control-Max-Age", "3600");
//
//         chain.doFilter(request, resp);
//     }
//}
