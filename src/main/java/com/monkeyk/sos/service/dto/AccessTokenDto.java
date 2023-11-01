package com.monkeyk.sos.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;


import java.io.Serial;
import java.io.Serializable;
import java.time.temporal.ChronoField;

/**
 * 2019/7/5
 * <p>
 * {"access_token":"iuy0fbfe-da2c-4840-8b66-848168ad8d00","token_type":"bearer","refresh_token":"9406e12f-d62e-42bd-ad40-0206d94ae7ds","expires_in":7199,"scope":"read"}
 *
 * @author Shengzhao Li
 * @since 2.0.1
 */
public class AccessTokenDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8894979171517528312L;


    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("expires_in")
    private int expiresIn;


    public AccessTokenDto() {
    }


    public AccessTokenDto(OAuth2AccessToken token) {
        this(token, null);
    }

    /**
     * @since 3.0.0
     */
    public AccessTokenDto(OAuth2AccessToken token, OAuth2RefreshToken refreshToken) {
        this.accessToken = token.getTokenValue();
        this.expiresIn = token.getExpiresAt().get(ChronoField.SECOND_OF_DAY);

        this.scope = StringUtils.join(token.getScopes(), ",");
        this.tokenType = token.getTokenType().getValue();

        this.refreshToken = refreshToken != null ? refreshToken.getTokenValue() : null;
//        final OAuth2RefreshToken oAuth2RefreshToken = token.getRefreshToken();
//        if (oAuth2RefreshToken != null) {
//            this.refreshToken = oAuth2RefreshToken.getValue();
//        }
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", scope='" + scope + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }

}
