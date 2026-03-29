package com.monkeyk.sos.service.dto;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.infrastructure.DateUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Passkey 凭据展示 DTO。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyCredentialDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1844787201036429156L;

    /**
     * 凭据 ID。
     */
    private String credentialId;

    /**
     * 用户设置的设备标签。
     */
    private String label;

    /**
     * 传输方式列表，逗号分隔。
     */
    private String transports;

    /**
     * 当前保存的签名计数器。
     */
    private long signCount;

    /**
     * 创建时间文本。
     */
    private String createTime;

    /**
     * 最后使用时间文本。
     */
    private String lastUsedTime;

    public PasskeyCredentialDto() {
    }

    public PasskeyCredentialDto(PasskeyCredential credential) {
        this.credentialId = credential.credentialId();
        this.label = credential.label();
        this.transports = credential.transports();
        this.signCount = credential.signCount();
        this.createTime = DateUtils.toDateTime(credential.createTime());
        LocalDateTime lastUsed = credential.lastUsedTime();
        if (lastUsed != null) {
            this.lastUsedTime = DateUtils.toDateTime(lastUsed);
        }
    }

    public static List<PasskeyCredentialDto> toDtos(List<PasskeyCredential> credentials) {
        List<PasskeyCredentialDto> dtos = new ArrayList<>(credentials.size());
        for (PasskeyCredential credential : credentials) {
            dtos.add(new PasskeyCredentialDto(credential));
        }
        return dtos;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
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

    public long getSignCount() {
        return signCount;
    }

    public void setSignCount(long signCount) {
        this.signCount = signCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(String lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }
}
