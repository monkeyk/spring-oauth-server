package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.domain.passkey.PasskeyCredentialRepository;
import com.monkeyk.sos.domain.shared.security.SOSUserDetails;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.domain.user.UserRepository;
import com.monkeyk.sos.infrastructure.PasskeyUtils;
import com.monkeyk.sos.service.PasskeyService;
import com.monkeyk.sos.service.business.PasskeyAuthenticationProcessor;
import com.monkeyk.sos.service.business.PasskeyRegistrationProcessor;
import com.monkeyk.sos.service.dto.CredentialDescriptorDto;
import com.monkeyk.sos.service.dto.PasskeyAssertionOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyAuthenticationRequestDto;
import com.monkeyk.sos.service.dto.PasskeyCredentialDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationRequestDto;
import com.monkeyk.sos.service.dto.PublicKeyCredentialParamDto;
import com.monkeyk.sos.web.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Passkey 业务服务实现。
 * <p>
 * 负责 options 组装、用户查询与当前用户 Passkey 管理；
 * 具体的注册校验与登录校验逻辑下沉到 service.business 对象中。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Service("passkeyService")
public class PasskeyServiceImpl implements PasskeyService {

    private static final Logger LOG = LoggerFactory.getLogger(PasskeyServiceImpl.class);

    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasskeyRegistrationProcessor passkeyRegistrationProcessor;

    @Autowired
    private PasskeyAuthenticationProcessor passkeyAuthenticationProcessor;

    /**
     * 为当前登录用户生成注册 Passkey 的参数。
     */
    @Override
    public PasskeyRegistrationOptionsDto prepareRegistrationOptions(String username, String challenge, String rpId, String rpName) {
        User user = requireUser(username);

        PasskeyRegistrationOptionsDto dto = new PasskeyRegistrationOptionsDto();
        dto.setChallenge(challenge);
        dto.setRp(new PasskeyRegistrationOptionsDto.RpDto(rpName, rpId));
        dto.setUser(new PasskeyRegistrationOptionsDto.UserDto(
                PasskeyUtils.base64UrlEncode(PasskeyUtils.utf8Bytes(user.guid())),
                user.username(),
                StringUtils.defaultIfBlank(user.nickname(), user.username())));

        List<PublicKeyCredentialParamDto> params = new ArrayList<>();
        params.add(new PublicKeyCredentialParamDto(-7));
        params.add(new PublicKeyCredentialParamDto(-257));
        dto.setPubKeyCredParams(params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Prepare Passkey registration options(username: {})", WebUtils.getIp(), username);
        }
        return dto;
    }

    /**
     * 校验注册结果并保存 Passkey 凭据。
     */
    @Override
    public void registerPasskey(String username, PasskeyRegistrationRequestDto requestDto,
                                String challenge, String origin, String rpId) {
        User user = requireUser(username);
        passkeyRegistrationProcessor.register(user, requestDto, challenge, origin, rpId);
    }

    /**
     * 生成登录页使用的 Passkey assertion 参数。
     */
    @Override
    public PasskeyAssertionOptionsDto prepareAssertionOptions(String username, String challenge, String rpId) {
        User user = requireUser(username);
        List<PasskeyCredential> credentials = passkeyCredentialRepository.findPasskeyCredentialsByUserGuid(user.guid());
        if (credentials.isEmpty()) {
            LOG.warn("{}|Current user has no passkeys for username: {}", WebUtils.getIp(), username);
            throw new IllegalStateException("Current user has no passkeys");
        }

        PasskeyAssertionOptionsDto dto = new PasskeyAssertionOptionsDto();
        dto.setChallenge(challenge);
        dto.setRpId(rpId);
        List<CredentialDescriptorDto> allowCredentials = new ArrayList<>();
        for (PasskeyCredential credential : credentials) {
            allowCredentials.add(new CredentialDescriptorDto(credential.credentialId(), credential.transports()));
        }
        dto.setAllowCredentials(allowCredentials);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Prepare Passkey assertion options(username: {}, size: {})", WebUtils.getIp(),
                    username, allowCredentials.size());
        }
        return dto;
    }

    /**
     * 校验 Passkey 登录结果并返回可用于登录的用户名。
     */
    @Override
    public String authenticate(String username, PasskeyAuthenticationRequestDto requestDto,
                               String challenge, String origin, String rpId) {
        User user = requireUser(username);
        passkeyAuthenticationProcessor.authenticate(user, requestDto, challenge, origin, rpId);
        return user.username();
    }

    /**
     * 查询当前登录用户已绑定的全部 Passkey。
     */
    @Override
    public List<PasskeyCredentialDto> loadCurrentUserPasskeys() {
        SOSUserDetails userDetails = currentUserDetails();
        List<PasskeyCredential> credentials = passkeyCredentialRepository.findPasskeyCredentialsByUserGuid(userDetails.getUserGuid());
        return PasskeyCredentialDto.toDtos(credentials);
    }

    /**
     * 删除当前用户指定的 Passkey。
     */
    @Override
    public void deleteCurrentUserPasskey(String credentialId) {
        SOSUserDetails userDetails = currentUserDetails();
        passkeyCredentialRepository.deletePasskeyCredential(credentialId, userDetails.getUserGuid());
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Delete Passkey(userGuid: {}, credentialId: {})", WebUtils.getIp(),
                    userDetails.getUserGuid(), credentialId);
        }
    }

    /**
     * 获取当前登录用户详情。
     */
    private SOSUserDetails currentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication == null ? null : authentication.getPrincipal();
        if (!(principal instanceof SOSUserDetails)) {
            throw new IllegalStateException("Current user is not authenticated");
        }
        return (SOSUserDetails) principal;
    }

    /**
     * 根据用户名加载并校验用户是否可用。
     */
    private User requireUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.archived() || !user.enabled()) {
            LOG.warn("{}|Invalid user for passkey, username: {}", WebUtils.getIp(), username);
            throw new IllegalStateException("Invalid user for passkey");
        }
        return user;
    }
}
