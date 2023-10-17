/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.user.Privilege;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.domain.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2015/11/16
 *
 * @author Shengzhao Li
 */
@Repository("userRepositoryJdbc")
public class UserRepositoryJdbc implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryJdbc.class);

    private final UserRowMapper userRowMapper = new UserRowMapper();

    /**
     * @since 3.0.0
     */
    private final UserProfileRowMapper userProfileRowMapper = new UserProfileRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByGuid(String guid) {
        final String sql = " select * from user_ where  guid = ? ";
        final List<User> list = this.jdbcTemplate.query(sql, userRowMapper, guid);

        User user = null;
        if (!list.isEmpty()) {
            user = list.get(0);
            user.privileges().addAll(findPrivileges(user.id()));
        }

        return user;
    }

    private Collection<Privilege> findPrivileges(long userId) {
        final String sql = " select privilege from user_privilege where user_id = ? ";
        final List<String> strings = this.jdbcTemplate.queryForList(sql, String.class, userId);

        List<Privilege> privileges = new ArrayList<>(strings.size());
        privileges.addAll(strings.stream().map(Privilege::valueOf).collect(Collectors.toList()));
        return privileges;
    }

    @Override
    public void saveUser(final User user) {
        final String sql = " insert into user_(guid,archived,create_time,email,password,username,phone," +
                "address,nickname,updated_at,enabled) " +
                " values (?,?,?,?,?,?,?,?,?,?,?) ";
        this.jdbcTemplate.update(sql, ps -> {
            ps.setString(1, user.guid());
            ps.setBoolean(2, user.archived());

            ps.setTimestamp(3, Timestamp.valueOf(user.createTime()));
            ps.setString(4, user.email());

            ps.setString(5, user.password());
            ps.setString(6, user.username());

            ps.setString(7, user.phone());
            // v3.0.0 added
            ps.setString(8, user.address());
            ps.setString(9, user.nickname());
            ps.setLong(10, user.updatedAt());
            ps.setBoolean(11, user.enabled());
        });

        //get user id
        final Integer id = this.jdbcTemplate.queryForObject("select id from user_ where guid = ?", Integer.class, user.guid());

        //insert privileges
        for (final Privilege privilege : user.privileges()) {
            this.jdbcTemplate.update("insert into user_privilege(user_id, privilege) values (?,?)", ps -> {
                ps.setInt(1, id);
                ps.setString(2, privilege.name());
            });
        }

    }

    @Override
    public void updateUser(final User user) {
        final String sql = " update user_ set username = ?, password = ?, phone = ?,email = ?," +
                "address = ?, nickname = ?, enabled = ? where guid = ? ";
        int row = this.jdbcTemplate.update(sql, ps -> {
            ps.setString(1, user.username());
            ps.setString(2, user.password());

            ps.setString(3, user.phone());
            ps.setString(4, user.email());
            // v3.0.0 added
            ps.setString(5, user.address());
            ps.setString(6, user.nickname());
            ps.setBoolean(7, user.enabled());

            ps.setString(8, user.guid());
        });
    }

    @Override
    public User findByUsername(String username) {
        final String sql = " select * from user_ where username = ? and archived = 0 ";
        final List<User> list = this.jdbcTemplate.query(sql, userRowMapper, username);

        User user = null;
        if (!list.isEmpty()) {
            user = list.get(0);
            user.privileges().addAll(findPrivileges(user.id()));
        }
        if (list.size() > 1) {
            LOG.warn("Found {} user(s) by username: {}, checking duplicate data??", list.size(), username);
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findProfileByUsername(String username) {
        final String sql = " select id, guid,create_time,archived, username,enabled,phone,email,address,nickname,updated_at from user_ where username = ? and archived = 0 ";
        final List<User> list = this.jdbcTemplate.query(sql, userProfileRowMapper, username);

        User user = null;
        if (!list.isEmpty()) {
            user = list.get(0);
        }
        if (list.size() > 1) {
            LOG.warn("Found {} user profiles by username: {}, checking duplicate data??", list.size(), username);
        }

        return user;
    }

    @Override
    public List<User> findUsersByUsername(String username) {
        String sql = " select * from user_ where archived = 0 ";
        Object[] params = new Object[]{};
        if (StringUtils.isNotEmpty(username)) {
            sql += " and username like ?";
            params = new Object[]{"%" + username + "%"};
        }
        sql += " order by create_time desc ";

        final List<User> list = this.jdbcTemplate.query(sql, userRowMapper, params);
        for (User user : list) {
            user.privileges().addAll(findPrivileges(user.id()));
        }
        return list;
    }
}
