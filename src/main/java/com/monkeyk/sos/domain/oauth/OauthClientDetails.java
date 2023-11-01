package com.monkeyk.sos.domain.oauth;

import com.monkeyk.sos.infrastructure.DateUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * table: oauth2_registered_client
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.client.RegisteredClient
 * @since 1.0.0
 */
public class OauthClientDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = -6947822646185526939L;


    /**
     * 对应数据库中的 id 字段
     *
     * @since 3.0.0
     */
    private String id;


    /**
     * 创建时间，系统管理
     */
    private LocalDateTime createTime = DateUtils.now();

    /**
     * 逻辑删除标识：true表示已经删除，默认false
     */
    private boolean archived = false;

    private String clientId;

    /**
     * client 名称，
     * 一般由添加时填写
     *
     * @since 3.0.0
     */
    private String clientName;


    /**
     * client 签发时间，一般指创建时间
     *
     * @since 3.0.0
     */
    private Instant clientIdIssuedAt = Instant.now();

    /**
     * Encrypted 加密存储
     */
    private String clientSecret;

    /**
     * secret 过期时间，
     * null则无过期；
     * 可用于一些临时签发使用
     *
     * @since 3.0.0
     */
    private Instant clientSecretExpiresAt;


    /**
     * 认证支持的方式，多个由逗号分隔
     * 如: client_secret_basic,client_secret_post
     *
     * @see org.springframework.security.oauth2.core.ClientAuthenticationMethod
     * @since 3.0.0
     */
    private String clientAuthenticationMethods;


    /**
     * OIDC scope 值, 多个由逗号分隔
     * 如: openid,profile,email
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes
     */
    private String scopes;


    /**
     * 授权支持的 grant_type (OAuth2.1), 多个由逗号分隔
     * 如: authorization_code,refresh_token
     *
     * @see org.springframework.security.oauth2.core.AuthorizationGrantType
     */
    private String authorizationGrantTypes;

    /**
     * OAuth2 认证后回调uri， 一般传递code, 多个由逗号分隔
     * The re-direct URI(s) established during registration (optional, comma separated).
     */
    private String redirectUris;


    /**
     * OAuth2 退出时 post 的客户端重定向 uri，可选
     * 多个由逗号分隔
     * 在client注册时可填写
     *
     * @since 3.0.0
     */
    private String postLogoutRedirectUris;


    /**
     * 客户端的各类设置
     * 如是否支持PKCE，用户授权(consent)确认是否必须
     * 必须由 {ClientSettings} 生成的字符串
     *
     * @see org.springframework.security.oauth2.server.authorization.settings.ClientSettings
     * @since 3.0.0
     */
    private String clientSettings;

    /**
     * token的各类设置
     * 如 token有效期，refresh_token有效期
     * 必须由 {TokenSettings} 生成的字符串
     *
     * @see org.springframework.security.oauth2.server.authorization.settings.TokenSettings
     * @since 3.0.0
     */
    private String tokenSettings;


    public OauthClientDetails() {
    }


    /**
     * @since 3.0.0
     */
    public String id() {
        return id;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails id(String id) {
        this.id = id;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String tokenSettings() {
        return tokenSettings;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails tokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String clientSettings() {
        return clientSettings;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails clientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
        return this;
    }


    public LocalDateTime createTime() {
        return createTime;
    }

    public OauthClientDetails createTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public boolean archived() {
        return archived;
    }

    public String clientId() {
        return clientId;
    }


    public String clientSecret() {
        return clientSecret;
    }

    /**
     * @since 3.0.0
     */
    public String clientName() {
        return clientName;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public Instant clientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails clientIdIssuedAt(Instant clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public Instant clientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails clientSecretExpiresAt(Instant clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String clientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails clientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
        return this;
    }

    public String scopes() {
        return scopes;
    }

    public OauthClientDetails scopes(String scopes) {
        this.scopes = scopes;
        return this;
    }

    public String authorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public OauthClientDetails authorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
        return this;
    }

    public String redirectUris() {
        return redirectUris;
    }

    public OauthClientDetails redirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String postLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    /**
     * @since 3.0.0
     */
    public OauthClientDetails postLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OauthClientDetails");
        sb.append("{createTime=").append(createTime);
        sb.append(", archived=").append(archived);
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", clientName='").append(clientName).append('\'');
        sb.append(", scopes='").append(scopes).append('\'');
        sb.append(", authorizationGrantTypes='").append(authorizationGrantTypes).append('\'');
        sb.append(", redirectUris='").append(redirectUris).append('\'');
        sb.append(", clientIdIssuedAt='").append(clientIdIssuedAt).append('\'');
        sb.append(", clientSettings=").append(clientSettings);
        sb.append(", tokenSettings=").append(tokenSettings);
        sb.append(", postLogoutRedirectUris='").append(postLogoutRedirectUris).append('\'');
        sb.append(", clientAuthenticationMethods=").append(clientAuthenticationMethods);
        sb.append('}');
        return sb.toString();
    }

    public OauthClientDetails clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public OauthClientDetails clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }


    public OauthClientDetails archived(boolean archived) {
        this.archived = archived;
        return this;
    }
}