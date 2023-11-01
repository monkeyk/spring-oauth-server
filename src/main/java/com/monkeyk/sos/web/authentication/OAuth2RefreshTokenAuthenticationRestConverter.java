package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 2023/10/31 10:33
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter
 * @since 3.0.0
 */
public final class OAuth2RefreshTokenAuthenticationRestConverter extends AbstractAuthenticationRestConverter {


    @Override
    public Authentication convert(Map<String, String> parameters) {
        // grant_type (REQUIRED)
        String grantType = parameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

//        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // refresh_token (REQUIRED)
        String refreshToken = parameters.get(OAuth2ParameterNames.REFRESH_TOKEN);
        if (!StringUtils.hasText(refreshToken)) {
            throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.REFRESH_TOKEN,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // scope (OPTIONAL)
        String scope = parameters.get(OAuth2ParameterNames.SCOPE);
//        if (!StringUtils.hasText(scope)) {
//            throwError(
//                    OAuth2ErrorCodes.INVALID_REQUEST,
//                    OAuth2ParameterNames.SCOPE,
//                    ACCESS_TOKEN_REQUEST_ERROR_URI);
//        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.REFRESH_TOKEN) &&
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value);
            }
        });

        return new OAuth2RefreshTokenAuthenticationToken(
                refreshToken, clientPrincipal, requestedScopes, additionalParameters);
    }
}
