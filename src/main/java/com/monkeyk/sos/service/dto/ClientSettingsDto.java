package com.monkeyk.sos.service.dto;

import com.monkeyk.sos.infrastructure.SettingsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.io.Serial;
import java.io.Serializable;

import static com.monkeyk.sos.domain.shared.SOSConstants.HS;
import static org.springframework.security.oauth2.jose.jws.JwsAlgorithms.RS256;

/**
 * 2023/10/13 11:52
 * <p>
 * .requireProofKey(false)
 * .requireAuthorizationConsent(false);
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.settings.ClientSettings
 * @since 3.0.0
 */
public class ClientSettingsDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7335241589844569340L;

    /**
     * 支持PKCE为true
     * 默认false
     */
    private boolean requireProofKey;

    /**
     * 授权需要用户进行确认为true
     * 默认false
     */
    private boolean requireAuthorizationConsent;

    /**
     * 若client有自定义的 jwk URL，
     * 则填写, jwt-bearer流程中会使用到(OAuth2.1新增)
     *
     * @since 3.0.0
     */
    private String jwkSetUrl;

    /**
     * 设置生成  jwt token的算法，
     * 可选值来自 JwsAlgorithm
     *
     * @see JwsAlgorithm
     */
    private String tokenEndpointAuthenticationSigningAlgorithm = RS256;


    public ClientSettingsDto() {
    }

    public ClientSettingsDto(String clientSettings) {
        ClientSettings settings = SettingsUtils.buildClientSettings(clientSettings);
        this.requireAuthorizationConsent = settings.isRequireAuthorizationConsent();
        this.requireProofKey = settings.isRequireProofKey();

        JwsAlgorithm jAlg = settings.getTokenEndpointAuthenticationSigningAlgorithm();
        if (jAlg != null) {
            this.tokenEndpointAuthenticationSigningAlgorithm = jAlg.getName();
        }
        this.jwkSetUrl = settings.getJwkSetUrl();
    }

    public ClientSettings toSettings() {
        ClientSettings.Builder builder = ClientSettings.builder()
                .requireProofKey(requireProofKey)
                .requireAuthorizationConsent(requireAuthorizationConsent);
        //区分不同算法:对称/非对称
        if (tokenEndpointAuthenticationSigningAlgorithm.startsWith(HS)) {
            builder.tokenEndpointAuthenticationSigningAlgorithm(MacAlgorithm.valueOf(tokenEndpointAuthenticationSigningAlgorithm));
        } else {
            builder.tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.valueOf(tokenEndpointAuthenticationSigningAlgorithm));
        }
        if (StringUtils.isNotBlank(jwkSetUrl)) {
            builder.jwkSetUrl(jwkSetUrl);
        }
        return builder.build();
    }


    public boolean isRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    public boolean isRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    public String getTokenEndpointAuthenticationSigningAlgorithm() {
        return tokenEndpointAuthenticationSigningAlgorithm;
    }

    public void setTokenEndpointAuthenticationSigningAlgorithm(String tokenEndpointAuthenticationSigningAlgorithm) {
        this.tokenEndpointAuthenticationSigningAlgorithm = tokenEndpointAuthenticationSigningAlgorithm;
    }

    @Override
    public String toString() {
        return "{" +
                "requireProofKey=" + requireProofKey +
                ", requireAuthorizationConsent=" + requireAuthorizationConsent +
//                ", jwkSetUrl='" + jwkSetUrl + '\'' +
                ", tokenEndpointAuthenticationSigningAlgorithm='" + tokenEndpointAuthenticationSigningAlgorithm + '\'' +
                '}';
    }
}
