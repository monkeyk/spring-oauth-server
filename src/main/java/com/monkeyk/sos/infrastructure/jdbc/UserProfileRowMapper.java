package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * table: user_
 * 2023/10/17
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public class UserProfileRowMapper implements RowMapper<User> {


    public UserProfileRowMapper() {
    }

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();

        user.id(rs.getInt("id"));
        user.guid(rs.getString("guid"));

        user.archived(rs.getBoolean("archived"));
        user.createTime(rs.getTimestamp("create_time").toLocalDateTime());

        user.email(rs.getString("email"));
        user.phone(rs.getString("phone"));
        user.username(rs.getString("username"));

        user.address(rs.getString("address"));
        user.nickname(rs.getString("nickname"));
        user.enabled(rs.getBoolean("enabled"));
        user.updatedAt(rs.getLong("updated_at"));

        return user;
    }
}
