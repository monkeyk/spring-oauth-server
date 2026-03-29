package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.domain.passkey.PasskeyCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Passkey 仓储的 JDBC 实现。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Repository("passkeyCredentialRepositoryJdbc")
public class PasskeyCredentialRepositoryJdbc implements PasskeyCredentialRepository {

    private final PasskeyCredentialRowMapper rowMapper = new PasskeyCredentialRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存新的 Passkey 凭据记录。
     */
    @Override
    public void savePasskeyCredential(PasskeyCredential credential) {
        final String sql = " insert into passkey_credential(guid,create_time,user_guid,credential_id,public_key,public_key_algorithm,sign_count,label,transports,last_used_time) values (?,?,?,?,?,?,?,?,?,?) ";
        this.jdbcTemplate.update(sql, ps -> {
            int index = 1;
            ps.setString(index++, credential.guid());
            ps.setTimestamp(index++, Timestamp.valueOf(credential.createTime()));
            ps.setString(index++, credential.userGuid());
            ps.setString(index++, credential.credentialId());
            ps.setString(index++, credential.publicKey());
            ps.setInt(index++, credential.publicKeyAlgorithm());
            ps.setLong(index++, credential.signCount());
            ps.setString(index++, credential.label());
            ps.setString(index++, credential.transports());
            ps.setTimestamp(index, credential.lastUsedTime() != null ? Timestamp.valueOf(credential.lastUsedTime()) : null);
        });
    }

    /**
     * 查询某个用户已绑定的全部 Passkey。
     */
    @Override
    public List<PasskeyCredential> findPasskeyCredentialsByUserGuid(String userGuid) {
        final String sql = " select * from passkey_credential where user_guid = ? order by create_time desc ";
        return this.jdbcTemplate.query(sql, rowMapper, userGuid);
    }

    /**
     * 按 credentialId 查询单个 Passkey。
     */
    @Override
    public PasskeyCredential findPasskeyCredential(String credentialId) {
        final String sql = " select * from passkey_credential where credential_id = ? ";
        final List<PasskeyCredential> list = this.jdbcTemplate.query(sql, rowMapper, credentialId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 更新 Passkey 的签名计数器。
     */
    @Override
    public void updatePasskeyCredentialCounter(String credentialId, long signCount) {
        final String sql = " update passkey_credential set sign_count = ?, last_used_time = now() where credential_id = ? ";
        this.jdbcTemplate.update(sql, signCount, credentialId);
    }

    /**
     * 删除用户下的指定 Passkey。
     */
    @Override
    public void deletePasskeyCredential(String credentialId, String userGuid) {
        final String sql = " delete from passkey_credential where credential_id = ? and user_guid = ? ";
        this.jdbcTemplate.update(sql, credentialId, userGuid);
    }
}
