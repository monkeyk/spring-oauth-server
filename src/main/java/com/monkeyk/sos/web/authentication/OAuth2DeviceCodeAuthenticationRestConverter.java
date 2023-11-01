package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceCodeAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 2023/10/31 10:33
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2DeviceCodeAuthenticationConverter
 * @since 3.0.0
 */
public final class OAuth2DeviceCodeAuthenticationRestConverter extends AbstractAuthenticationRestConverter {


    @Override
    public Authentication convert(Map<String, String> parameters) {
        // grant_type (REQUIRED)
        String grantType = parameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.DEVICE_CODE.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

//        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // device_code (REQUIRED)
        String deviceCode = parameters.get(OAuth2ParameterNames.DEVICE_CODE);
        if (!StringUtils.hasText(deviceCode)) {
            throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.DEVICE_CODE,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.DEVICE_CODE)) {
                additionalParameters.put(key, value);
            }
        });

        return new OAuth2DeviceCodeAuthenticationToken(deviceCode, clientPrincipal, additionalParameters);
    }
}
