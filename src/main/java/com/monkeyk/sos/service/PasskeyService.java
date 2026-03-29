package com.monkeyk.sos.service;

import com.monkeyk.sos.service.dto.PasskeyAssertionOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyAuthenticationRequestDto;
import com.monkeyk.sos.service.dto.PasskeyCredentialDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationRequestDto;

import java.util.List;

/**
 * Passkey 业务服务接口。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public interface PasskeyService {

    /**
     * 为已登录用户生成注册 Passkey 所需的 options。
     */
    PasskeyRegistrationOptionsDto prepareRegistrationOptions(String username, String challenge, String rpId, String rpName);

    /**
     * 校验并保存用户注册的 Passkey。
     */
    void registerPasskey(String username, PasskeyRegistrationRequestDto requestDto,
                         String challenge, String origin, String rpId);

    /**
     * 为登录页生成 Passkey 登录所需的 assertion options。
     */
    PasskeyAssertionOptionsDto prepareAssertionOptions(String username, String challenge, String rpId);

    /**
     * 校验 Passkey 登录结果，成功后返回登录用户名。
     */
    String authenticate(String username, PasskeyAuthenticationRequestDto requestDto,
                        String challenge, String origin, String rpId);

    /**
     * 查询当前登录用户的全部 Passkey。
     */
    List<PasskeyCredentialDto> loadCurrentUserPasskeys();

    /**
     * 删除当前用户的某个 Passkey。
     */
    void deleteCurrentUserPasskey(String credentialId);
}
