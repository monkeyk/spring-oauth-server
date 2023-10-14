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

import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.domain.oauth.OauthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * 2015/11/16
 *
 * @author Shengzhao Li
 */
@Repository("oauthRepositoryJdbc")
public class OauthRepositoryJdbc implements OauthRepository {


    private final OauthClientDetailsRowMapper oauthClientDetailsRowMapper = new OauthClientDetailsRowMapper();


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public OauthClientDetails findOauthClientDetails(String clientId) {
        final String sql = " select * from oauth2_registered_client where  client_id = ? ";
        final List<OauthClientDetails> list = this.jdbcTemplate.query(sql, oauthClientDetailsRowMapper, clientId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OauthClientDetails> findAllOauthClientDetails() {
        final String sql = " select * from oauth2_registered_client where archived = 0  order by create_time desc ";
        return this.jdbcTemplate.query(sql, oauthClientDetailsRowMapper);
    }

    @Override
    public void updateOauthClientDetailsArchive(String clientId, boolean archive) {
        final String sql = " update oauth2_registered_client set archived = ? where client_id = ? ";
        this.jdbcTemplate.update(sql, archive, clientId);
    }

    @Override
    public void saveOauthClientDetails(final OauthClientDetails clientDetails) {
        final String sql = " insert into oauth2_registered_client(id,create_time,client_id,client_id_issued_at,client_secret,client_secret_expires_at," +
                "client_name,client_authentication_methods,authorization_grant_types,redirect_uris," +
                " post_logout_redirect_uris,scopes,client_settings,token_settings) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        this.jdbcTemplate.update(sql, ps -> {
            int index = 1;
            ps.setString(index++, clientDetails.id());
            ps.setTimestamp(index++, Timestamp.valueOf(clientDetails.createTime()));
            ps.setString(index++, clientDetails.clientId());
            ps.setTimestamp(index++, Timestamp.from(clientDetails.clientIdIssuedAt()));

            ps.setString(index++, clientDetails.clientSecret());
            Instant clientSecretExpiresAt = clientDetails.clientSecretExpiresAt();
            ps.setTimestamp(index++, clientSecretExpiresAt != null ? Timestamp.from(clientSecretExpiresAt) : null);
            ps.setString(index++, clientDetails.clientName());

            ps.setString(index++, clientDetails.clientAuthenticationMethods());
            ps.setString(index++, clientDetails.authorizationGrantTypes());
            ps.setString(index++, clientDetails.redirectUris());

            ps.setString(index++, clientDetails.postLogoutRedirectUris());
            ps.setString(index++, clientDetails.scopes());
            ps.setString(index++, clientDetails.clientSettings());

            ps.setString(index++, clientDetails.tokenSettings());
        });
    }
}
