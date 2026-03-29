package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册 Passkey 时传给浏览器的 publicKey options。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyRegistrationOptionsDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7874842432751758829L;

    /**
     * RP 基本信息。
     */
    private RpDto rp;

    /**
     * 当前用户基础信息。
     */
    private UserDto user;

    /**
     * 本次注册 challenge。
     */
    private String challenge;

    /**
     * 超时时间，毫秒。
     */
    private long timeout = 60000L;

    /**
     * attestation 策略。
     */
    private String attestation = "none";

    /**
     * 期望的认证器附着方式。
     */
    private String authenticatorAttachment;

    /**
     * resident key 策略。
     */
    private String residentKey = "preferred";

    /**
     * 用户验证等级。
     */
    private String userVerification = "preferred";

    /**
     * 允许浏览器选择的公钥算法。
     */
    private List<PublicKeyCredentialParamDto> pubKeyCredParams = new ArrayList<>();

    public RpDto getRp() {
        return rp;
    }

    public void setRp(RpDto rp) {
        this.rp = rp;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public String getAuthenticatorAttachment() {
        return authenticatorAttachment;
    }

    public void setAuthenticatorAttachment(String authenticatorAttachment) {
        this.authenticatorAttachment = authenticatorAttachment;
    }

    public String getResidentKey() {
        return residentKey;
    }

    public void setResidentKey(String residentKey) {
        this.residentKey = residentKey;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }

    public List<PublicKeyCredentialParamDto> getPubKeyCredParams() {
        return pubKeyCredParams;
    }

    public void setPubKeyCredParams(List<PublicKeyCredentialParamDto> pubKeyCredParams) {
        this.pubKeyCredParams = pubKeyCredParams;
    }

    public static class RpDto implements Serializable {
        @Serial
        private static final long serialVersionUID = 7803707384962419909L;
        /**
         * RP 展示名称。
         */
        private String name;

        /**
         * RP ID。
         */
        private String id;

        public RpDto() {
        }

        public RpDto(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class UserDto implements Serializable {
        @Serial
        private static final long serialVersionUID = 1835257557386995391L;
        /**
         * 用户唯一标识，Base64URL 编码。
         */
        private String id;

        /**
         * 用户名。
         */
        private String name;

        /**
         * 展示名称。
         */
        private String displayName;

        public UserDto() {
        }

        public UserDto(String id, String name, String displayName) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }
}
