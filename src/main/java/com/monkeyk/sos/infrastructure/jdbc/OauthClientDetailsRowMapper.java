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
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * table: oauth2_registered_client
 * 2015/11/16
 *
 * @author Shengzhao Li
 */
public class OauthClientDetailsRowMapper implements RowMapper<OauthClientDetails> {


    public OauthClientDetailsRowMapper() {
    }

    @Override
    public OauthClientDetails mapRow(ResultSet rs, int i) throws SQLException {
        OauthClientDetails clientDetails = new OauthClientDetails();

        clientDetails.id(rs.getString("id"));
        clientDetails.archived(rs.getBoolean("archived"));
        clientDetails.createTime(rs.getTimestamp("create_time").toLocalDateTime());
        clientDetails.clientId(rs.getString("client_id"));
        clientDetails.clientIdIssuedAt(rs.getTimestamp("client_id_issued_at").toInstant());
        clientDetails.clientName(rs.getString("client_name"));

        clientDetails.clientAuthenticationMethods(rs.getString("client_authentication_methods"));
        clientDetails.clientSecret(rs.getString("client_secret"));

        clientDetails.scopes(rs.getString("scopes"));
        clientDetails.authorizationGrantTypes(rs.getString("authorization_grant_types"));
        clientDetails.redirectUris(rs.getString("redirect_uris"));

        clientDetails.postLogoutRedirectUris(rs.getString("post_logout_redirect_uris"));
        clientDetails.clientSettings(rs.getString("client_settings"));
        clientDetails.tokenSettings(rs.getString("token_settings"));

        Timestamp secretExpiresAt = rs.getTimestamp("client_secret_expires_at");
        if (secretExpiresAt != null) {
            clientDetails.clientSecretExpiresAt(secretExpiresAt.toInstant());
        }

        return clientDetails;
    }


    private Integer getInteger(ResultSet rs, String columnName) throws SQLException {
        final Object object = rs.getObject(columnName);
        if (object != null) {
            return (Integer) object;
        }
        return null;
    }

}
