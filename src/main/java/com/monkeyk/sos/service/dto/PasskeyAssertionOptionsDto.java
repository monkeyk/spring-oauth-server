package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录时传给浏览器的 Passkey assertion options。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyAssertionOptionsDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5574745476257627124L;

    /**
     * 本次登录 challenge。
     */
    private String challenge;

    /**
     * RP ID。
     */
    private String rpId;

    /**
     * 超时时间，毫秒。
     */
    private long timeout = 60000L;

    /**
     * 用户验证等级。
     */
    private String userVerification = "preferred";

    /**
     * 当前用户允许使用的凭据列表。
     */
    private List<CredentialDescriptorDto> allowCredentials = new ArrayList<>();

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }

    public List<CredentialDescriptorDto> getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(List<CredentialDescriptorDto> allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
