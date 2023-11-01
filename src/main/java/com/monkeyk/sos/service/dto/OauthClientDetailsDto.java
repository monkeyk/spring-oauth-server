package com.monkeyk.sos.service.dto;

import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.domain.shared.GuidGenerator;
import com.monkeyk.sos.infrastructure.DateUtils;
import com.monkeyk.sos.infrastructure.PasswordHandler;
import com.monkeyk.sos.infrastructure.SettingsUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;

/**
 * @author Shengzhao Li
 * @since 1.0.0
 */
public class OauthClientDetailsDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4011292111995231569L;


    /**
     * 对应数据库中的 id 字段
     *
     * @since 3.0.0
     */
    private String id;

    private String createTime;
    private boolean archived;

    private String clientId = GuidGenerator.generate();


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
    private String clientIdIssuedAt;


    private String clientSecret = GuidGenerator.generateClientSecret();


    /**
     * secret 过期时间，
     * null则无过期；
     * 可用于一些临时签发使用
     *
     * @since 3.0.0
     */
    private String clientSecretExpiresAt;


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
    private ClientSettingsDto clientSettings;

    /**
     * token的各类设置
     * 如 token有效期，refresh_token有效期
     * 必须由 {TokenSettings} 生成的字符串
     *
     * @see org.springframework.security.oauth2.server.authorization.settings.TokenSettings
     * @since 3.0.0
     */
    private TokenSettingsDto tokenSettings;


    public OauthClientDetailsDto() {
    }

    public OauthClientDetailsDto(OauthClientDetails clientDetails) {
        this.clientId = clientDetails.clientId();
        this.clientSecret = clientDetails.clientSecret();
        this.scopes = clientDetails.scopes();

        this.createTime = DateUtils.toDateTime(clientDetails.createTime());
        this.archived = clientDetails.archived();
        this.postLogoutRedirectUris = clientDetails.postLogoutRedirectUris();

        this.redirectUris = clientDetails.redirectUris();
        this.clientIdIssuedAt = clientDetails.clientIdIssuedAt().toString();
        Instant clientSecretExpiresAt1 = clientDetails.clientSecretExpiresAt();
        if (clientSecretExpiresAt1 != null) {
            this.clientSecretExpiresAt = clientSecretExpiresAt1.toString();
        }

        this.clientAuthenticationMethods = clientDetails.clientAuthenticationMethods();
        this.clientName = clientDetails.clientName();
        this.id = clientDetails.id();

        this.authorizationGrantTypes = clientDetails.authorizationGrantTypes();
        this.clientSettings = new ClientSettingsDto(clientDetails.clientSettings());
        this.tokenSettings = new TokenSettingsDto(clientDetails.tokenSettings());
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public static List<OauthClientDetailsDto> toDtos(List<OauthClientDetails> clientDetailses) {
        List<OauthClientDetailsDto> dtos = new ArrayList<>(clientDetailses.size());
        for (OauthClientDetails clientDetailse : clientDetailses) {
            dtos.add(new OauthClientDetailsDto(clientDetailse));
        }
        return dtos;
    }


    public boolean isContainsAuthorizationCode() {
        if (!this.authorizationGrantTypes.contains(AUTHORIZATION_CODE.getValue())) {
            return false;
        }
        if (clientSettings == null) {
            return true;
        }
        return !clientSettings.isRequireProofKey();
    }

    /**
     * PKCE   flow
     *
     * @since 3.0.0
     */
    public boolean isContainsAuthorizationCodeWithPKCE() {
        if (!this.authorizationGrantTypes.contains(AUTHORIZATION_CODE.getValue())) {
            return false;
        }
        return clientSettings != null && clientSettings.isRequireProofKey();
    }

    /**
     * OAuth2.1不支持
     *
     * @deprecated from OAuth2.1
     */
    public boolean isContainsPassword() {
        return this.authorizationGrantTypes.contains(PASSWORD.getValue());
    }

//    public boolean isContainsImplicit() {
//        return this.authorizationGrantTypes.contains("implicit");
//    }

    public boolean isContainsClientCredentials() {
        return this.authorizationGrantTypes.contains(CLIENT_CREDENTIALS.getValue());
    }

    public boolean isContainsRefreshToken() {
        return this.authorizationGrantTypes.contains(REFRESH_TOKEN.getValue());
    }

    /**
     * @since 3.0.0
     */
    public boolean isContainsDeviceCode() {
        return this.authorizationGrantTypes.contains(DEVICE_CODE.getValue());
    }

    /**
     * @since 3.0.0
     */
    public boolean isContainsJwtBearer() {
        return this.authorizationGrantTypes.contains(JWT_BEARER.getValue());
    }


    public OauthClientDetails createDomain() {
        OauthClientDetails clientDetails = new OauthClientDetails()
                .id(GuidGenerator.generateNumber())
                .clientId(clientId)
                .clientName(clientName)
                // encrypted client secret
                .clientSecret(PasswordHandler.encode(clientSecret))
                .postLogoutRedirectUris(postLogoutRedirectUris)
                .authorizationGrantTypes(authorizationGrantTypes)
                .clientAuthenticationMethods(clientAuthenticationMethods)
                .scopes(scopes);

        if (StringUtils.isNotBlank(clientIdIssuedAt)) {
            clientDetails.clientIdIssuedAt(Instant.parse(this.clientIdIssuedAt));
        }

        if (StringUtils.isNotBlank(clientSecretExpiresAt)) {
            clientDetails.clientSecretExpiresAt(Instant.parse(this.clientSecretExpiresAt));
        }

        if (StringUtils.isNotEmpty(redirectUris)) {
            clientDetails.redirectUris(redirectUris);
        }

        clientDetails.clientSettings(SettingsUtils.textClientSettings(this.clientSettings.toSettings()));
        clientDetails.tokenSettings(SettingsUtils.textTokenSettings(this.tokenSettings.toSettings()));

        return clientDetails;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(String clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(String clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }

    public ClientSettingsDto getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(ClientSettingsDto clientSettings) {
        this.clientSettings = clientSettings;
    }

    public TokenSettingsDto getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(TokenSettingsDto tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    /**
     * 逗号, 转化为 ' '
     *
     * @return scopes
     */
    public String getScopesWithBlank() {
        if (scopes != null && scopes.contains(",")) {
            return scopes.replaceAll(",", " ");
        }
        return scopes;
    }
}