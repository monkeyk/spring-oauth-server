/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.monkeyk.sos.web.controller;

import com.monkeyk.sos.config.OAuth2ServerConfiguration;
import com.monkeyk.sos.web.WebUtils;
import com.monkeyk.sos.web.authentication.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;

/**
 * 2016/3/8
 * <p/>
 * Restful OAuth API
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter
 * @since 2.0.0
 */
@Controller
public class OAuthRestController {


    private static final Logger LOG = LoggerFactory.getLogger(OAuthRestController.class);

    private static final String DEFAULT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private static final String CLIENT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";


    private final AuthenticationRestConverter authenticationConverter;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter =
            new OAuth2ErrorHttpMessageConverter();


    private AuthenticationManager authenticationManager;


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;


    public OAuthRestController() {

        this.authenticationConverter = new DelegatingAuthenticationRestConverter(
                Arrays.asList(
                        new OAuth2AuthorizationCodeAuthenticationRestConverter(),
                        new OAuth2RefreshTokenAuthenticationRestConverter(),
                        new OAuth2ClientCredentialsAuthenticationRestConverter(),
                        new OAuth2DeviceCodeAuthenticationRestConverter()));
    }

    /**
     * Replace OAuth2TokenEndpointFilter flow use restful API
     *
     * @param parameters request params
     * @see org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider
     */
    @PostMapping("/oauth2/rest_token")
    @ResponseBody
    public void postAccessToken(@RequestBody Map<String, String> parameters, HttpServletResponse response)
            throws OAuth2AuthenticationException, IOException {

        //init OAuth2 contexts
        initialOAuth2Contexts(parameters);

        // oauth2 flow start...
        String grantType = parameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (grantType == null) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.GRANT_TYPE);
        }

        Authentication authorizationGrantAuthentication = this.authenticationConverter.convert(parameters);
        if (authorizationGrantAuthentication == null) {
            throwError(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, OAuth2ParameterNames.GRANT_TYPE);
        }
        if (authorizationGrantAuthentication instanceof AbstractAuthenticationToken) {
            ((AbstractAuthenticationToken) authorizationGrantAuthentication)
//                        .setDetails(this.authenticationDetailsSource.buildDetails(request));
                    .setDetails(new WebAuthenticationDetails(WebUtils.getIp(), null));
        }

        checkAndInitialAuthenticationManager();

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) this.authenticationManager.authenticate(authorizationGrantAuthentication);
        this.sendAccessTokenResponse(response, accessTokenAuthentication);
    }

    private void initialOAuth2Contexts(Map<String, String> parameters) {
        String clientId = parameters.get(OAuth2ParameterNames.CLIENT_ID);
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Retrieved registered client");
        }

        if (!registeredClient.getClientAuthenticationMethods().contains(
                ClientAuthenticationMethod.CLIENT_SECRET_POST)) {
            throwInvalidClient("authentication_method");
        }

        String clientSecret = parameters.get(OAuth2ParameterNames.CLIENT_SECRET);
        if (clientSecret == null) {
            throwInvalidClient("credentials");
        }

//        String clientSecret = clientAuthentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_SECRET);
        }

        if (registeredClient.getClientSecretExpiresAt() != null &&
                Instant.now().isAfter(registeredClient.getClientSecretExpiresAt())) {
            throwInvalidClient("client_secret_expires_at");
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Authenticated client secret");
        }
        OAuth2ClientAuthenticationToken authentication = new OAuth2ClientAuthenticationToken(registeredClient,
                ClientAuthenticationMethod.CLIENT_SECRET_POST, clientSecret);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // init AuthorizationServerContext
        AuthorizationServerContext authorizationServerContext = new AuthorizationServerContext() {
            @Override
            public String getIssuer() {
                return authorizationServerSettings.getIssuer();
            }

            @Override
            public AuthorizationServerSettings getAuthorizationServerSettings() {
                return authorizationServerSettings;
            }
        };
        AuthorizationServerContextHolder.setContext(authorizationServerContext);
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public void handleOAuth2AuthenticationException(OAuth2AuthenticationException ex, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Token request failed: {}", ex.getError(), ex);
        }
        this.sendErrorResponse(response, ex);
    }


    private void checkAndInitialAuthenticationManager() {
        if (this.authenticationManager == null) {
            OAuth2ServerConfiguration serverConfiguration = applicationContext.getBean(OAuth2ServerConfiguration.class);
            this.authenticationManager = serverConfiguration.authenticationManagerOAuth2();
            Assert.notNull(this.authenticationManager, "authenticationManager cannot be null");
        }
    }


    private void sendErrorResponse(HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {

        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        this.errorHttpResponseConverter.write(error, null, httpResponse);
    }

    private void sendAccessTokenResponse(HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType())
                        .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }


    private static void throwError(String errorCode, String parameterName) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, DEFAULT_ERROR_URI);
        throw new OAuth2AuthenticationException(error);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName,
                CLIENT_ERROR_URI
        );
        throw new OAuth2AuthenticationException(error);
    }

}
