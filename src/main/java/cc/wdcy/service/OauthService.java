package cc.wdcy.service;

import cc.wdcy.domain.dto.OauthClientDetailsDto;
import cc.wdcy.domain.oauth.OauthClientDetails;

import java.util.List;

/**
 * @author Shengzhao Li
 */

public interface OauthService {

    OauthClientDetails loadOauthClientDetails(String clientId);

    List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos();

    void archiveOauthClientDetails(String clientId);

    OauthClientDetailsDto loadOauthClientDetailsDto(String clientId);

    void registerClientDetails(OauthClientDetailsDto formDto);
}