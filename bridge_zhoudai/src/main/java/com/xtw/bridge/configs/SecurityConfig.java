package com.xtw.bridge.configs;

import com.xtw.bridge.filter.JwtAuthenticationTokenFilter;
import com.xtw.bridge.service.authentication.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: SpringSecurity配置类
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private DataSource dataSource;      // 这个数据源就是application.yml中配置的那个数据源
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * formLogin登录认证方式不需要写controller方法，因为它是通过UsernamePasswordAuthenticationFilter过滤器进行验证，这个过滤器
         * 在引入SpringSecurity核心包时就默认集成到项目中了，只需要针对它进行配置即可
         */
        http
                .csrf().disable()       // 开启csrf跨站攻击防御,关闭的话在后面加上 .disable()
             //    // 用Cookie存储CSRF令牌
             //    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
             //    // 忽略一些请求不防御，比如登录认证请求
             //    .ignoringAntMatchers("/users/login")
             // .and()
                .cors()     // 开启CORS跨域访问配置
             .and()
                // 让自定义的jwtAuthenticationTokenFilter在UsernamePasswordAuthenticationFilter前面执行
                // 如果jwtAuthenticationTokenFilter认证通过，UsernamePasswordAuthenticationFilter就不会再执行了
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()   // 退出登录功能
            .and()
                .rememberMe()       // 记住我功能
                // 设置form表单中"自动登录"勾选框的参数名，这样ajax请求中data中的参数名也要改为remember-me-new
                .rememberMeParameter("remember-me-new")
                // 设置保存在浏览器端的cookie的名称
                .rememberMeCookieName("remember-me-cookie")
                // 设置cookie的有效期。默认是2周(这里设置成2天)
                .tokenValiditySeconds(2*24*6*60)
                // 将Token中的相关信息存到数据库中
                .tokenRepository(persistentTokenRepository())
            .and()
                .authorizeRequests()
                .antMatchers("/users/login", "/users/refreshtoken", "/druid/**", "/swagger-ui.html","/swagger-ui/**"
                    ,"/swagger-resources/**","/v3/**", "/device/fibreTemperatures"
                ).permitAll()    //表示访问这里的资源不用经过认证
                // 所有请求都要通过使用这个access方法里面传递的表达式的规则进行校验，如果返回true允许访问
                .anyRequest().access("@rbacService.hasPermission(request,authentication)")
            .and()
                .sessionManagement()    // Session管理
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);   // Session创建策略，默认是需要时没有才创建
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService)       // 动态加载用户权限信息
                .passwordEncoder(passwordEncoder());                // 配置BCrypt加密
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 静态资源的访问权限控制
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将项目中的静态资源路径开放出来。这里的资源不会经过 过滤器的验证就会被开放
        web.ignoring().antMatchers();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();    // 通过JDBC的方式操作数据库中的Token
        tokenRepository.setDataSource(dataSource);      // 使用这个数据源
        return tokenRepository;
    }

    // 将AuthenticationManager注册成一个Bean
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 配置CORS跨域访问
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));    // 允许哪些源访问当前域的资源（// http://192.168.100.4:8888）
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));     // 开放哪些http方法，允许跨域访问
        configuration.applyPermitDefaultValues();   // 设置其它一些默认值
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);     // 针对哪些请求资源
        return source;
    }
}
