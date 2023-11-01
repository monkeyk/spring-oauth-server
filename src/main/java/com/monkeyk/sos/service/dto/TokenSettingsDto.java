package com.monkeyk.sos.service.dto;

import com.monkeyk.sos.infrastructure.SettingsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * 2023/10/13 12:07
 * <p>
 * <p>
 * .authorizationCodeTimeToLive(Duration.ofMinutes(5))
 * .accessTokenTimeToLive(Duration.ofMinutes(5))
 * .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
 * .deviceCodeTimeToLive(Duration.ofMinutes(5))
 * .reuseRefreshTokens(true)
 * .refreshTokenTimeToLive(Duration.ofMinutes(60))
 * .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256);
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.settings.TokenSettings
 * @since 3.0.0
 */
public class TokenSettingsDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8918978047051059724L;


    /**
     * authorizationCode 有效时长，单位：秒
     * 默认 300 （5分钟)
     */
    private long authorizationCodeTimeToLive = 300L;


    /**
     * access_token 有效时长，单位：秒
     * 默认 3600 （1小时)
     */
    private long accessTokenTimeToLive = 3600L;

    /**
     * token 格式,两个值
     * self-contained       -> jwt
     * reference              -> uuid
     *
     * @see OAuth2TokenFormat
     */
    private String accessTokenFormat = "self-contained";


    /**
     * device_code 有效时长，单位：秒
     * 默认 300 （5分钟）
     */
    private long deviceCodeTimeToLive = 300L;


    /**
     * 是否复用 refresh_token 在刷新后
     * 默认 true
     */
    private boolean reuseRefreshTokens = true;

    /**
     * refresh_token 有效时长，单位：秒
     * 默认 43200（12小时）
     */
    private long refreshTokenTimeToLive = 43200L;

    /**
     * id_token签名使用的算法
     * 注意：设置的算法需要 jwks 支持
     *
     * @see SignatureAlgorithm
     */
    private String idTokenSignatureAlgorithm;


    public TokenSettingsDto() {
    }

    public TokenSettingsDto(String tokenSettings) {
        TokenSettings settings = SettingsUtils.buildTokenSettings(tokenSettings);
        this.accessTokenFormat = settings.getAccessTokenFormat().getValue();
        this.idTokenSignatureAlgorithm = settings.getIdTokenSignatureAlgorithm().getName();

        this.refreshTokenTimeToLive = settings.getRefreshTokenTimeToLive().toSeconds();
        this.accessTokenFormat= settings.getAccessTokenFormat().getValue();
        this.accessTokenTimeToLive=settings.getAccessTokenTimeToLive().toSeconds();

        this.deviceCodeTimeToLive= settings.getDeviceCodeTimeToLive().toSeconds();
        this.authorizationCodeTimeToLive= settings.getAuthorizationCodeTimeToLive().toSeconds();

    }

    public TokenSettings toSettings() {
        TokenSettings.Builder builder = TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofSeconds(this.refreshTokenTimeToLive))
                .accessTokenTimeToLive(Duration.ofSeconds(this.accessTokenTimeToLive))
                .reuseRefreshTokens(this.reuseRefreshTokens)
                .deviceCodeTimeToLive(Duration.ofSeconds(this.deviceCodeTimeToLive))
                .authorizationCodeTimeToLive(Duration.ofSeconds(this.authorizationCodeTimeToLive));
        if (StringUtils.isNotBlank(idTokenSignatureAlgorithm)) {
            builder.idTokenSignatureAlgorithm(SignatureAlgorithm.valueOf(idTokenSignatureAlgorithm));
        }
        if (StringUtils.isNotBlank(accessTokenFormat)) {
            builder.accessTokenFormat(new OAuth2TokenFormat(accessTokenFormat));
        }

        return builder.build();
    }


    public long getAuthorizationCodeTimeToLive() {
        return authorizationCodeTimeToLive;
    }

    public void setAuthorizationCodeTimeToLive(long authorizationCodeTimeToLive) {
        this.authorizationCodeTimeToLive = authorizationCodeTimeToLive;
    }

    public long getAccessTokenTimeToLive() {
        return accessTokenTimeToLive;
    }

    public void setAccessTokenTimeToLive(long accessTokenTimeToLive) {
        this.accessTokenTimeToLive = accessTokenTimeToLive;
    }

    public String getAccessTokenFormat() {
        return accessTokenFormat;
    }

    public void setAccessTokenFormat(String accessTokenFormat) {
        this.accessTokenFormat = accessTokenFormat;
    }

    public long getDeviceCodeTimeToLive() {
        return deviceCodeTimeToLive;
    }

    public void setDeviceCodeTimeToLive(long deviceCodeTimeToLive) {
        this.deviceCodeTimeToLive = deviceCodeTimeToLive;
    }

    public boolean isReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public long getRefreshTokenTimeToLive() {
        return refreshTokenTimeToLive;
    }

    public void setRefreshTokenTimeToLive(long refreshTokenTimeToLive) {
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    public String getIdTokenSignatureAlgorithm() {
        return idTokenSignatureAlgorithm;
    }

    public void setIdTokenSignatureAlgorithm(String idTokenSignatureAlgorithm) {
        this.idTokenSignatureAlgorithm = idTokenSignatureAlgorithm;
    }

    @Override
    public String toString() {
        return "{" +
                "authorizationCodeTimeToLive=" + authorizationCodeTimeToLive +
                ", accessTokenTimeToLive=" + accessTokenTimeToLive +
                ", accessTokenFormat='" + accessTokenFormat + '\'' +
                ", deviceCodeTimeToLive=" + deviceCodeTimeToLive +
                ", reuseRefreshTokens=" + reuseRefreshTokens +
                ", refreshTokenTimeToLive=" + refreshTokenTimeToLive +
                ", idTokenSignatureAlgorithm='" + idTokenSignatureAlgorithm + '\'' +
                '}';
    }
}
