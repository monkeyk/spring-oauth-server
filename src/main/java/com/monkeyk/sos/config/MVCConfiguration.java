package com.monkeyk.sos.config;


import com.monkeyk.sos.web.filter.CharacterEncodingIPFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;

/**
 * 2018/1/30
 * <p>
 * Spring MVC 扩展配置
 * <p>
 *
 * @author Shengzhao Li
 * @since 2.0.0
 */
@Configuration
public class MVCConfiguration implements WebMvcConfigurer {


    /**
     * 扩展拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        WebMvcConfigurer.super.addInterceptors(registry);
    }


    /**
     * 解决乱码问题
     * For UTF-8
     * <p>
     * Spring Framework 7 起 {@link WebMvcConfigurer#configureMessageConverters(List)} 已过时待删除，
     * 且 Spring Boot 4 的默认转换器（含 Jackson JSON）改为通过
     * {@link HttpMessageConverters.ServerBuilder} 钩子注册；若继续只覆盖 List 版本，
     * 会使转换器列表非空从而跳过全部默认转换器注册，导致所有 @ResponseBody 接口返回 406。
     */
    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.withStringConverter(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }


    /**
     * 字符编码配置 UTF-8
     */
    @Bean
    public FilterRegistrationBean<Filter> encodingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CharacterEncodingIPFilter());
        registrationBean.addUrlPatterns("/*");
        //值越小越靠前
        registrationBean.setOrder(1);
        return registrationBean;
    }


}
