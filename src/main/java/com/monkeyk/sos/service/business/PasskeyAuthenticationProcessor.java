package com.monkeyk.sos.service.business;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.domain.passkey.PasskeyCredentialRepository;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.infrastructure.PasskeyUtils;
import com.monkeyk.sos.service.dto.PasskeyAuthenticationRequestDto;
import com.monkeyk.sos.web.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Passkey 登录处理对象。
 * <p>
 * 负责 assertion 校验、签名验证与计数器更新。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Component
public class PasskeyAuthenticationProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(PasskeyAuthenticationProcessor.class);

    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;


    public PasskeyAuthenticationProcessor() {
    }


    /**
     * 校验 Passkey 登录结果。
     */
    public void authenticate(User user, PasskeyAuthenticationRequestDto requestDto,
                             String challenge, String origin, String rpId) {
        validateAuthenticationRequest(requestDto);
        if (StringUtils.isBlank(challenge)) {
            LOG.warn("{}|Passkey authentication challenge missing for username: {}", WebUtils.getIp(), user.username());
            throw new IllegalStateException("Passkey authentication challenge is missing");
        }

        PasskeyCredential credential = passkeyCredentialRepository.findPasskeyCredential(requestDto.getCredentialId());
        if (credential == null || !StringUtils.equals(user.guid(), credential.userGuid())) {
            LOG.warn("{}|Passkey credential not found for username: {}, credentialId: {}", WebUtils.getIp(),
                    user.username(), requestDto.getCredentialId());
            throw new IllegalStateException("Passkey credential not found");
        }

        PasskeyUtils.verifyAuthentication(challenge, origin, rpId,
                requestDto.getClientDataJSON(), requestDto.getAuthenticatorData());
        PasskeyUtils.verifySignature(credential.publicKey(), credential.publicKeyAlgorithm(),
                requestDto.getAuthenticatorData(), requestDto.getClientDataJSON(), requestDto.getSignature());

        long newSignCount = PasskeyUtils.authenticatorData(requestDto.getAuthenticatorData()).signCount();
        if (newSignCount > 0 && credential.signCount() > 0 && newSignCount <= credential.signCount()) {
            LOG.warn("{}|Passkey signCount verify failed for username: {}, credentialId: {}", WebUtils.getIp(),
                    user.username(), credential.credentialId());
            throw new IllegalStateException("Passkey signCount verify failed");
        }
        passkeyCredentialRepository.updatePasskeyCredentialCounter(credential.credentialId(), newSignCount);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Authenticate Passkey(username: {}, credentialId: {}) success", WebUtils.getIp(),
                    user.username(), credential.credentialId());
        }
    }


    /**
     * 校验登录请求参数是否完整。
     */
    private void validateAuthenticationRequest(PasskeyAuthenticationRequestDto requestDto) {
        if (requestDto == null
                || StringUtils.isBlank(requestDto.getCredentialId())
                || StringUtils.isBlank(requestDto.getClientDataJSON())
                || StringUtils.isBlank(requestDto.getAuthenticatorData())
                || StringUtils.isBlank(requestDto.getSignature())) {
            throw new IllegalStateException("Invalid passkey authentication request");
        }
    }
}
