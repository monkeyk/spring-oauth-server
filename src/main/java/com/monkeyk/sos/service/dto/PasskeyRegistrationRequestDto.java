package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 浏览器提交的 Passkey 注册结果请求体。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyRegistrationRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6319464107489335649L;

    /**
     * 凭据 ID。
     */
    private String credentialId;

    /**
     * 浏览器导出的公钥内容。
     */
    private String publicKey;

    /**
     * 公钥算法编号。
     */
    private int publicKeyAlgorithm;

    /**
     * 浏览器返回的 clientDataJSON。
     */
    private String clientDataJSON;

    /**
     * 浏览器返回的 authenticatorData。
     */
    private String authenticatorData;

    /**
     * 用户自定义设备标签。
     */
    private String label;

    /**
     * 浏览器返回的 transports 信息。
     */
    private String transports;

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getPublicKeyAlgorithm() {
        return publicKeyAlgorithm;
    }

    public void setPublicKeyAlgorithm(int publicKeyAlgorithm) {
        this.publicKeyAlgorithm = publicKeyAlgorithm;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTransports() {
        return transports;
    }

    public void setTransports(String transports) {
        this.transports = transports;
    }
}
