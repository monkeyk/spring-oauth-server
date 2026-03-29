package com.monkeyk.sos.domain.passkey;

import com.monkeyk.sos.domain.AbstractDomain;

import java.time.LocalDateTime;

/**
 * Passkey 凭据实体。
 * <p>
 * 一条记录对应用户绑定的一个 WebAuthn 凭据，
 * 用于后续无密码登录时进行公钥验签与计数器校验。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyCredential extends AbstractDomain {

    /**
     * 关联的用户 GUID。
     */
    private String userGuid;

    /**
     * 浏览器返回的凭据唯一标识。
     */
    private String credentialId;

    /**
     * 凭据对应的公钥内容，使用 Base64URL 编码保存。
     */
    private String publicKey;

    /**
     * 公钥算法。
     * 常见值：-7(ES256)、-257(RS256)。
     */
    private int publicKeyAlgorithm;

    /**
     * WebAuthn 签名计数器。
     */
    private long signCount;

    /**
     * 用户自定义设备标签，便于页面展示与管理。
     */
    private String label;

    /**
     * 浏览器返回的 transports 信息，逗号分隔保存。
     */
    private String transports;

    /**
     * 最近一次使用该 Passkey 登录的时间。
     */
    private LocalDateTime lastUsedTime;


    public PasskeyCredential() {
    }

    public String userGuid() {
        return userGuid;
    }

    public PasskeyCredential userGuid(String userGuid) {
        this.userGuid = userGuid;
        return this;
    }

    public String credentialId() {
        return credentialId;
    }

    public PasskeyCredential credentialId(String credentialId) {
        this.credentialId = credentialId;
        return this;
    }

    public String publicKey() {
        return publicKey;
    }

    public PasskeyCredential publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public int publicKeyAlgorithm() {
        return publicKeyAlgorithm;
    }

    public PasskeyCredential publicKeyAlgorithm(int publicKeyAlgorithm) {
        this.publicKeyAlgorithm = publicKeyAlgorithm;
        return this;
    }

    public long signCount() {
        return signCount;
    }

    public PasskeyCredential signCount(long signCount) {
        this.signCount = signCount;
        return this;
    }

    public String label() {
        return label;
    }

    public PasskeyCredential label(String label) {
        this.label = label;
        return this;
    }

    public String transports() {
        return transports;
    }

    public PasskeyCredential transports(String transports) {
        this.transports = transports;
        return this;
    }

    public LocalDateTime lastUsedTime() {
        return lastUsedTime;
    }

    /**
     * 给父类中的创建时间赋值，便于 RowMapper 回填数据库值。
     */
    public PasskeyCredential createTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public PasskeyCredential lastUsedTime(LocalDateTime lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
        return this;
    }

    @Override
    public String toString() {
        return "PasskeyCredential{" +
                "userGuid='" + userGuid + '\'' +
                ", credentialId='" + credentialId + '\'' +
                ", publicKeyAlgorithm=" + publicKeyAlgorithm +
                ", signCount=" + signCount +
                ", label='" + label + '\'' +
                ", transports='" + transports + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
