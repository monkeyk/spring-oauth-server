package com.monkeyk.sos.service.business;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.domain.passkey.PasskeyCredentialRepository;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.infrastructure.DateUtils;
import com.monkeyk.sos.infrastructure.PasskeyUtils;
import com.monkeyk.sos.service.dto.PasskeyRegistrationRequestDto;
import com.monkeyk.sos.web.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Passkey 注册处理对象。
 * <p>
 * 负责注册结果校验与凭据落库。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Component
public class PasskeyRegistrationProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(PasskeyRegistrationProcessor.class);

    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;


    public PasskeyRegistrationProcessor() {
    }


    /**
     * 校验注册结果并保存 Passkey 凭据。
     */
    public void register(User user, PasskeyRegistrationRequestDto requestDto,
                         String challenge, String origin, String rpId) {
        validateRegistrationRequest(requestDto);
        if (StringUtils.isBlank(challenge)) {
            LOG.warn("{}|Passkey registration challenge missing for username: {}", WebUtils.getIp(), user.username());
            throw new IllegalStateException("Passkey registration challenge is missing");
        }

        PasskeyUtils.verifyRegistration(challenge, origin, rpId,
                requestDto.getClientDataJSON(), requestDto.getAuthenticatorData());

        PasskeyUtils.loadPublicKey(requestDto.getPublicKey(), requestDto.getPublicKeyAlgorithm());

        if (passkeyCredentialRepository.findPasskeyCredential(requestDto.getCredentialId()) != null) {
            LOG.warn("{}|Passkey already existed for username: {}, credentialId: {}", WebUtils.getIp(),
                    user.username(), requestDto.getCredentialId());
            throw new IllegalStateException("Passkey already existed");
        }

        long signCount = PasskeyUtils.authenticatorData(requestDto.getAuthenticatorData()).signCount();
        PasskeyCredential credential = new PasskeyCredential()
                .userGuid(user.guid())
                .credentialId(requestDto.getCredentialId())
                .publicKey(requestDto.getPublicKey())
                .publicKeyAlgorithm(requestDto.getPublicKeyAlgorithm())
                .signCount(signCount)
                .label(StringUtils.defaultIfBlank(requestDto.getLabel(), "Passkey-" + DateUtils.now().toLocalDate().toString()))
                .transports(requestDto.getTransports())
                .lastUsedTime(DateUtils.now());
        passkeyCredentialRepository.savePasskeyCredential(credential);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Register Passkey(username: {}, credentialId: {}) success", WebUtils.getIp(),
                    user.username(), credential.credentialId());
        }
    }


    /**
     * 校验注册请求参数是否完整。
     */
    private void validateRegistrationRequest(PasskeyRegistrationRequestDto requestDto) {
        if (requestDto == null
                || StringUtils.isBlank(requestDto.getCredentialId())
                || StringUtils.isBlank(requestDto.getPublicKey())
                || StringUtils.isBlank(requestDto.getClientDataJSON())
                || StringUtils.isBlank(requestDto.getAuthenticatorData())) {
            throw new IllegalStateException("Invalid passkey registration request");
        }
    }
}
