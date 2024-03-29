package com.monkeyk.sos.domain.oauth;

import com.monkeyk.sos.domain.shared.Repository;

import java.util.List;

/**
 * @author Shengzhao Li
 * @since 1.0.0
 */
public interface OauthRepository extends Repository {

    OauthClientDetails findOauthClientDetails(String clientId);

    List<OauthClientDetails> findAllOauthClientDetails();

    void updateOauthClientDetailsArchive(String clientId, boolean archive);

    void saveOauthClientDetails(OauthClientDetails clientDetails);
}