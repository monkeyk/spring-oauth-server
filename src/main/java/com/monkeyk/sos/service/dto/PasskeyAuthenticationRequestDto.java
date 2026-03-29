package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 浏览器提交的 Passkey 登录请求体。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyAuthenticationRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7887577111772761869L;

    /**
     * 凭据 ID。
     */
    private String credentialId;

    /**
     * 浏览器返回的 clientDataJSON。
     */
    private String clientDataJSON;

    /**
     * 浏览器返回的 authenticatorData。
     */
    private String authenticatorData;

    /**
     * 浏览器返回的签名值。
     */
    private String signature;

    /**
     * 浏览器返回的 userHandle，可为空。
     */
    private String userHandle;

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getClientDataJSON() {
        return clientDataJSON;
    }

    public void setClientDataJSON(String clientDataJSON) {
        this.clientDataJSON = clientDataJSON;
    }

    public String getAuthenticatorData() {
        return authenticatorData;
    }

    public void setAuthenticatorData(String authenticatorData) {
        this.authenticatorData = authenticatorData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }
}
