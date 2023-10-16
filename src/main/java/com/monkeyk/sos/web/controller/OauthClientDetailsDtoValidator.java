package com.monkeyk.sos.web.controller;

import com.monkeyk.sos.service.dto.OauthClientDetailsDto;
import com.monkeyk.sos.service.OauthService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Shengzhao Li
 */
@Component
public class OauthClientDetailsDtoValidator implements Validator {


    @Autowired
    private OauthService oauthService;

    @Override
    public boolean supports(Class<?> clazz) {
        return OauthClientDetailsDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OauthClientDetailsDto clientDetailsDto = (OauthClientDetailsDto) target;

        validateClientId(clientDetailsDto, errors);
        validateClientSecret(clientDetailsDto, errors);

        validateGrantTypes(clientDetailsDto, errors);
        //v3.0.0 added
        validateClientName(clientDetailsDto, errors);
        validateScopes(clientDetailsDto, errors);
        validateMethods(clientDetailsDto, errors);
    }


    /**
     * @since 3.0.0
     */
    private void validateMethods(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        String methods = clientDetailsDto.getClientAuthenticationMethods();
        if (StringUtils.isBlank(methods)) {
            errors.reject(null, "authentication_methods is required");
        }
    }


    /**
     * @since 3.0.0
     */
    private void validateScopes(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        String scopes = clientDetailsDto.getScopes();
        if (StringUtils.isBlank(scopes)) {
            errors.reject(null, "scopes is required");
        } else if (!scopes.contains(OidcScopes.OPENID)) {
            errors.reject(null, "scopes [openid] must be selected");
        }
    }

    /**
     * @since 3.0.0
     */
    private void validateClientName(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        String clientName = clientDetailsDto.getClientName();
        if (StringUtils.isBlank(clientName)) {
            errors.reject(null, "client_name is required");
        }
    }

    private void validateGrantTypes(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        final String grantTypes = clientDetailsDto.getAuthorizationGrantTypes();
        if (StringUtils.isEmpty(grantTypes)) {
            errors.rejectValue("authorizationGrantTypes", null, "grant_type(s) is required");
            return;
        }

        if ("refresh_token".equalsIgnoreCase(grantTypes)) {
            errors.rejectValue("authorizationGrantTypes", null, "grant_type(s) 不能只是[refresh_token]");
        }
    }

    private void validateClientSecret(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        final String clientSecret = clientDetailsDto.getClientSecret();
        if (StringUtils.isEmpty(clientSecret)) {
            errors.rejectValue("clientSecret", null, "client_secret is required");
            return;
        }

        if (clientSecret.length() < 10) {
            errors.rejectValue("clientSecret", null, "client_secret 长度至少10位");
        }
    }

    private void validateClientId(OauthClientDetailsDto clientDetailsDto, Errors errors) {
        final String clientId = clientDetailsDto.getClientId();
        if (StringUtils.isEmpty(clientId)) {
            errors.rejectValue("clientId", null, "client_id is required");
            return;
        }

        if (clientId.length() < 10) {
            errors.rejectValue("clientId", null, "client_id 长度至少10位");
            return;
        }

        OauthClientDetailsDto clientDetailsDto1 = oauthService.loadOauthClientDetailsDto(clientId);
        if (clientDetailsDto1 != null) {
            errors.rejectValue("clientId", null, "client_id [" + clientId + "] 已存在");
        }

    }
}