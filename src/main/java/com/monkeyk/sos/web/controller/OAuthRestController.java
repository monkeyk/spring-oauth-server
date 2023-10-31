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

import com.monkeyk.sos.web.WebUtils;
import com.monkeyk.sos.web.authentication.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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
public class OAuthRestController implements InitializingBean, ApplicationContextAware {


    private static final Logger LOG = LoggerFactory.getLogger(OAuthRestController.class);

    private static final String DEFAULT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";


    private final AuthenticationRestConverter authenticationConverter;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter =
            new OAuth2ErrorHttpMessageConverter();


    private AuthenticationManager authenticationManager;

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
     */
    @PostMapping("/oauth2/rest_token")
    @ResponseBody
    public void postAccessToken(@RequestBody Map<String, String> parameters, HttpServletResponse response) throws IOException {

        try {
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

            OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                    (OAuth2AccessTokenAuthenticationToken) this.authenticationManager.authenticate(authorizationGrantAuthentication);
            this.sendAccessTokenResponse(response, accessTokenAuthentication);
        } catch (OAuth2AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Token request failed: {}", ex.getError(), ex);
            }
            this.sendErrorResponse(response, ex);
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


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(authenticationManager != null, "AuthenticationManager must be provided");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.authenticationManager == null) {
            AuthenticationConfiguration configuration = applicationContext.getBean(AuthenticationConfiguration.class);
            Assert.notNull(configuration, "AuthenticationManagerBuilder is null");
            try {
                this.authenticationManager = configuration.getAuthenticationManager();
            } catch (Exception e) {
                throw new BeanInitializationException("Call 'getAuthenticationManager' error", e);
            }
        }
    }

}
