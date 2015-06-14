package cc.wdcy.infrastructure.mybatis;

import cc.wdcy.domain.oauth.OauthClientDetails;
import cc.wdcy.domain.oauth.OauthRepository;
import cc.wdcy.domain.shared.GuidGenerator;
import cc.wdcy.infrastructure.AbstractRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * @author Shengzhao Li
 */
public class OauthRepositoryMyBatisTest extends AbstractRepositoryTest {


    @Autowired
    private OauthRepository oauthRepositoryMyBatis;


    @Test
    public void findOauthClientDetails() {
        OauthClientDetails oauthClientDetails = oauthRepositoryMyBatis.findOauthClientDetails("unity-client");
        assertNull(oauthClientDetails);

    }


    @Test
    public void saveOauthClientDetails() {

        final String clientId = GuidGenerator.generate();

        OauthClientDetails clientDetails = new OauthClientDetails().clientId(clientId);
        oauthRepositoryMyBatis.saveOauthClientDetails(clientDetails);

        final OauthClientDetails oauthClientDetails = oauthRepositoryMyBatis.findOauthClientDetails(clientId);
        assertNotNull(oauthClientDetails);
        assertNotNull(oauthClientDetails.clientId());
        assertNull(oauthClientDetails.clientSecret());

    }

    @Test
    public void findAllOauthClientDetails() {
        final List<OauthClientDetails> allOauthClientDetails = oauthRepositoryMyBatis.findAllOauthClientDetails();
        assertNotNull(allOauthClientDetails);
        assertTrue(allOauthClientDetails.isEmpty());
    }

    @Test
    public void updateOauthClientDetailsArchive() {
        oauthRepositoryMyBatis.updateOauthClientDetailsArchive("ddooelddd", true);
    }

}