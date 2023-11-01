package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 2023/10/31 10:33
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter
 * @since 3.0.0
 */
public final class OAuth2ClientCredentialsAuthenticationRestConverter extends AbstractAuthenticationRestConverter {


    @Override
    public Authentication convert(Map<String, String> parameters) {
        // grant_type (REQUIRED)
        String grantType = parameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

//        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // scope (OPTIONAL)
        String scope = parameters.get(OAuth2ParameterNames.SCOPE);
//        if (StringUtils.hasText(scope) &&
//                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
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
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value);
            }
        });

        return new OAuth2ClientCredentialsAuthenticationToken(
                clientPrincipal, requestedScopes, additionalParameters);
    }
}
