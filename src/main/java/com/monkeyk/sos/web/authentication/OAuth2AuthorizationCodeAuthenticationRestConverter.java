package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 2023/10/31 10:33
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter
 * @since 3.0.0
 */
public final class OAuth2AuthorizationCodeAuthenticationRestConverter extends AbstractAuthenticationRestConverter {


    @Override
    public Authentication convert(Map<String, String> parameters) {
        // grant_type (REQUIRED)
        String grantType = parameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

//        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // code (REQUIRED)
        String code = parameters.get(OAuth2ParameterNames.CODE);
        if (!StringUtils.hasText(code)) {
            throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.CODE,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // redirect_uri (REQUIRED)
        // Required only if the "redirect_uri" parameter was included in the authorization request
        String redirectUri = parameters.get(OAuth2ParameterNames.REDIRECT_URI);
        if (!StringUtils.hasText(redirectUri)) {
            throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.REDIRECT_URI,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE) &&
                    !key.equals(OAuth2ParameterNames.REDIRECT_URI)) {
//                additionalParameters.put(key, (value.size() == 1) ? value.get(0) : value.toArray(new String[0]));
                additionalParameters.put(key, value);
            }
        });

        return new OAuth2AuthorizationCodeAuthenticationToken(
                code, clientPrincipal, redirectUri, additionalParameters);
    }
}
