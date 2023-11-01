package com.monkeyk.sos.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static com.monkeyk.sos.config.OAuth2ServerConfiguration.KEYSTORE_NAME;
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

        Resource resource = new ClassPathResource(KEYSTORE_NAME);
        JWKSource<SecurityContext> jwkSource = JWKSourceBuilder.create(resource.getURL()).build();
        assertNotNull(jwkSource);

    }

}