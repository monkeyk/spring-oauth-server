package cc.wdcy.domain.oauth;

import cc.wdcy.domain.shared.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Shengzhao Li
 */
public interface OauthRepository extends Repository {

    OauthClientDetails findOauthClientDetails(String clientId);

    List<OauthClientDetails> findAllOauthClientDetails();

    void updateOauthClientDetailsArchive(@Param("clientId") String clientId, @Param("archive") boolean archive);

    void saveOauthClientDetails(OauthClientDetails clientDetails);
}