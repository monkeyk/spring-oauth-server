package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * PasskeyCredential 的 JDBC 行映射器。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyCredentialRowMapper implements RowMapper<PasskeyCredential> {

    /**
     * 将数据库记录转换为 PasskeyCredential 对象。
     */
    @Override
    public PasskeyCredential mapRow(ResultSet rs, int rowNum) throws SQLException {
        PasskeyCredential credential = new PasskeyCredential()
                .userGuid(rs.getString("user_guid"))
                .credentialId(rs.getString("credential_id"))
                .publicKey(rs.getString("public_key"))
                .publicKeyAlgorithm(rs.getInt("public_key_algorithm"))
                .signCount(rs.getLong("sign_count"))
                .label(rs.getString("label"))
                .transports(rs.getString("transports"));

        credential.id(rs.getLong("id"));
        credential.guid(rs.getString("guid"));

        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            credential.createTime(createTime.toLocalDateTime());
        }
        Timestamp lastUsedTime = rs.getTimestamp("last_used_time");
        if (lastUsedTime != null) {
            credential.lastUsedTime(lastUsedTime.toLocalDateTime());
        }
        return credential;
    }
}
