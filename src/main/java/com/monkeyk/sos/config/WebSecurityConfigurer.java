package com.monkeyk.sos.config;

import com.monkeyk.sos.web.context.SOSContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 2016/4/3
 * <p/>
 * Replace security.xml
 *
 * @author Shengzhao Li
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfigurer {


    /**
     * 需要调试时 可把此配置参数换为 true
     *
     * @since 3.0.0
     */
    @Value("${sos.spring.web.security.debug:false}")
    private boolean springWebSecurityDebug;


    /**
     * 扩展默认的 Web安全配置项
     * <p>
     * defaultSecurityFilterChain
     *
     * @throws Exception e
     * @since 3.0.0
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrfConfigurer -> {
            csrfConfigurer.ignoringRequestMatchers("/oauth2/rest_token");
        });

        http.authorizeHttpRequests(matcherRegistry -> {
            // permitAll() 的URL路径属于公开访问，不需要权限
            matcherRegistry
                    .requestMatchers("/favicon.ico*", "/oauth2/rest_token*", "*.js", "*.css").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/login*").anonymous()

                    // /user/ 开头的URL需要 ADMIN 权限
                    .requestMatchers("/user/**").hasAnyRole("ADMIN")
                    // 所有以 /unity/  开头的 URL属于 UNITY 权限
                    .requestMatchers("/unity/**").hasAnyRole("UNITY")
                    // 所有以 /m/  开头的 URL属于 MOBILE 权限
                    .requestMatchers("/m/**").hasAnyRole("MOBILE")
                    // anyRequest() 放最后
                    .anyRequest().authenticated();
        });

        http.formLogin(formLoginConfigurer -> {
            formLoginConfigurer
                    .loginPage("/login")
                    .loginProcessingUrl("/signin")
                    .failureUrl("/login?error_failed=true")
//                    .defaultSuccessUrl("/")
                    .usernameParameter("oidc_user")
                    .passwordParameter("oidcPwd");

        });

        http.logout(logoutConfigurer -> {
            logoutConfigurer.logoutUrl("/signout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/");
        });

//        http.sessionManagement(configurer -> {
//            configurer.maximumSessions(1).maxSessionsPreventsLogin(true);
//        });

//        http.exceptionHandling(configurer -> {
//            configurer.accessDeniedHandler((request, response, accessDeniedException) -> {
//                response.sendRedirect("/access_denied");
//            });
//        });

        return http.build();
    }


    /**
     * 安全配置自定义扩展
     *
     * @return WebSecurityCustomizer
     * @since 3.0.0
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(this.springWebSecurityDebug);
    }


    /**
     * BCrypt  加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * SOSContextHolder bean
     *
     * @return SOSContextHolder bean
     * @since 2.0.1
     */
    @Bean
    public SOSContextHolder sosContextHolder() {
        return new SOSContextHolder();
    }
}
