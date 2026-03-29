package com.monkeyk.sos.domain.passkey;

import com.monkeyk.sos.domain.shared.Repository;

import java.util.List;

/**
 * Passkey 持久化仓储接口。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public interface PasskeyCredentialRepository extends Repository {

    /**
     * 保存一个新的 Passkey 凭据。
     */
    void savePasskeyCredential(PasskeyCredential credential);

    /**
     * 查询指定用户下的全部 Passkey 凭据。
     */
    List<PasskeyCredential> findPasskeyCredentialsByUserGuid(String userGuid);

    /**
     * 按凭据 ID 查询单个 Passkey。
     */
    PasskeyCredential findPasskeyCredential(String credentialId);

    /**
     * 更新计数器并刷新最后使用时间。
     */
    void updatePasskeyCredentialCounter(String credentialId, long signCount);

    /**
     * 删除指定用户下的某个 Passkey。
     */
    void deletePasskeyCredential(String credentialId, String userGuid);
}
