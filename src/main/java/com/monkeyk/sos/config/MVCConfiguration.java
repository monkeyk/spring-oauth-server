package com.monkeyk.sos.config;


import com.monkeyk.sos.web.filter.CharacterEncodingIPFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
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
