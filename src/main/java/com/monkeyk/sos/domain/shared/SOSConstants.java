package com.monkeyk.sos.domain.shared;

/**
 * 2023/9/23 18:54
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public interface SOSConstants {

    /**
     * device verification URI
     *
     * @see org.springframework.security.oauth2.server.authorization.web.OAuth2DeviceVerificationEndpointFilter
     */
    String DEVICE_VERIFICATION_ENDPOINT_URI = "/oauth2/device_verification";


    /**
     * oauth2 consent page uri
     */
    String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    /**
     * oauth2 authorize uri
     *
     * @see org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter
     */
    String AUTHORIZATION_ENDPOINT_URI = "/oauth2/authorize";

    /**
     * 对称算法名称前缀，如HS256
     * 详见 MacAlgorithm.java
     */
    String HS = "HS";

}
