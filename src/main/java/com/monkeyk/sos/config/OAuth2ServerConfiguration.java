package com.monkeyk.sos.config;


import com.monkeyk.sos.domain.oauth.ClaimsOAuth2TokenCustomizer;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.io.IOException;
import java.util.function.Consumer;

import static com.monkeyk.sos.domain.shared.SOSConstants.CUSTOM_CONSENT_PAGE_URI;

/**
 * 2018/2/8
 * <p>
 * <p>
 * OAuth2 config
 *
 * @author Shengzhao Li
 */
@Configuration
public class OAuth2ServerConfiguration {


    /**
     * Fixed,  resource-id
     *
     * @deprecated Not used from v3.0.0
     */
    public static final String RESOURCE_ID = "sos-resource";


    /**
     * keystore file name
     *
     * @since 3.0.0
     */
    public static String KEYSTORE_NAME = "jwks.json";


    /**
     * @since 3.0.0
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @since 3.0.0
     */
    private AuthenticationManager authenticationManagerOAuth2;


    /**
     * authorizationServerSecurityFilterChain
     *
     * @since 3.0.0
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        });

        http.authorizeHttpRequests(registry -> {
            registry
                    // 所有以 /unity/  开头的 URL属于此资源
                    .requestMatchers("/unity/**").hasAnyRole("UNITY")
                    // 所有以 /m/  开头的 URL属于此资源
                    .requestMatchers("/m/**").hasAnyRole("MOBILE");
        });

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
//                        deviceAuthorizationEndpoint.verificationUri("/activate")
//                )
                .deviceVerificationEndpoint(deviceVerificationEndpoint ->
                        deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)
                )
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                // Enable OpenID Connect 1.0
                .oidc(oidcConfigurer -> {
                    oidcConfigurer.providerConfigurationEndpoint(endpointConfigurer -> {
                        //扩展oidc默认能力
                        endpointConfigurer.providerConfigurationCustomizer(oidcProviderConfigurationCustomizer());
                    });
                });

        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        //ext jwt
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));

        DefaultSecurityFilterChain filterChain = http.build();
        this.authenticationManagerOAuth2 = http.getSharedObject(AuthenticationManager.class);
        return filterChain;
    }


    /**
     * 获取 OAuth2流程中的  AuthenticationManager
     *
     * @return AuthenticationManager
     * @since 3.0.0
     */
    public AuthenticationManager authenticationManagerOAuth2() {
        return authenticationManagerOAuth2;
    }

    /**
     * 扩展 oidc 的默认能力配置项
     *
     * @since 3.0.0
     */
    private Consumer<OidcProviderConfiguration.Builder> oidcProviderConfigurationCustomizer() {
        return builder -> {
            builder.idTokenSigningAlgorithms(strings -> {
                        strings.add(SignatureAlgorithm.ES256.getName());
                    }).grantTypes(grantTypes -> {
                        //向下兼容添加，v3.0.0
//                        grantTypes.add(AuthorizationGrantType.PASSWORD.getValue());
                        grantTypes.add(AuthorizationGrantType.JWT_BEARER.getValue());
                    })
                    .scopes(strings -> {
                        strings.add(OidcScopes.PROFILE);
                        strings.add(OidcScopes.EMAIL);
                        strings.add(OidcScopes.ADDRESS);
                        strings.add(OidcScopes.PHONE);
                    });
        };
    }


    /**
     * 注册客户端管理
     *
     * @return RegisteredClientRepository
     * @since 3.0.0
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
//        return new InMemoryRegisteredClientRepository(client);
        return new JdbcRegisteredClientRepository(this.jdbcTemplate);
    }

    /**
     * 授权准许存储配置, jdbc实现
     *
     * @since 3.0.0
     */
    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(this.jdbcTemplate, registeredClientRepository);
    }


    /**
     * 授权信息存储配置, jdbc实现
     *
     * @since 3.0.0
     */
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(this.jdbcTemplate, registeredClientRepository);
    }


    /**
     * 提供加密/解密的 source
     * 可多个 key, 根据不同的需要来选择使用
     *
     * @return JWKSource
     * @since 3.0.0
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws IOException {

        Resource resource = new ClassPathResource(KEYSTORE_NAME);
        return JWKSourceBuilder.create(resource.getURL()).build();
    }


    /**
     * 扩展 jwt  id_token 等生成
     *
     * @since 3.0.0
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return new ClaimsOAuth2TokenCustomizer();
    }


}
