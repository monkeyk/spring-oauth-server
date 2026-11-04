package com.monkeyk.sos.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2023/10/12 17:58
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
class OAuth2ServerConfigurationTest {


    @Test
    void jwkSource() throws Exception {

        Resource resource = new ClassPathResource("jwks.json");
        JWKSource<SecurityContext> jwkSource = JWKSourceBuilder.create(resource.getURL()).build();
        assertNotNull(jwkSource);

    }

    /**
     * 测试 JwkSourceFactory 从 classpath 加载（开发默认模式）
     *
     * @since 3.1.0
     */
    @Test
    void jwkSourceFromClasspath() throws Exception {
        JwkProperties properties = new JwkProperties();
        // 不设置 value 和 filePath，回退到 classpath
        properties.setClasspath("jwks.json");

        JWKSource<SecurityContext> jwkSource = JwkSourceFactory.create(properties);
        assertNotNull(jwkSource);
    }

    /**
     * 测试 JwkSourceFactory 从 value 直接注入（生产推荐模式）
     *
     * @since 3.1.0
     */
    @Test
    void jwkSourceFromValue() throws Exception {
        Resource resource = new ClassPathResource("jwks.json");
        String jwksJson = new String(resource.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

        JwkProperties properties = new JwkProperties();
        properties.setValue(jwksJson);

        JWKSource<SecurityContext> jwkSource = JwkSourceFactory.create(properties);
        assertNotNull(jwkSource);
    }

}