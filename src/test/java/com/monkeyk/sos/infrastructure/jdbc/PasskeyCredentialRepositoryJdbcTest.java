package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.passkey.PasskeyCredential;
import com.monkeyk.sos.domain.passkey.PasskeyCredentialRepository;
import com.monkeyk.sos.domain.shared.GuidGenerator;
import com.monkeyk.sos.infrastructure.AbstractRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasskeyCredentialRepositoryJdbc 单元测试。
 * <p>
 * 参照现有 Repository 测试风格，覆盖保存、查询、更新与删除。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyCredentialRepositoryJdbcTest extends AbstractRepositoryTest {

    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;

    @Test
    public void saveAndFindPasskeyCredential() {
        String userGuid = GuidGenerator.generate();
        String credentialId = GuidGenerator.generate();

        PasskeyCredential credential = new PasskeyCredential()
                .userGuid(userGuid)
                .credentialId(credentialId)
                .publicKey("test-public-key")
                .publicKeyAlgorithm(-7)
                .signCount(8L)
                .label("MacBook Chrome")
                .transports("internal,hybrid");
        passkeyCredentialRepository.savePasskeyCredential(credential);

        PasskeyCredential result = passkeyCredentialRepository.findPasskeyCredential(credentialId);
        assertNotNull(result);
        assertEquals(userGuid, result.userGuid());
        assertEquals("test-public-key", result.publicKey());
        assertEquals(-7, result.publicKeyAlgorithm());
        assertEquals(8L, result.signCount());
        assertEquals("MacBook Chrome", result.label());
        assertEquals("internal,hybrid", result.transports());
    }

    @Test
    public void findPasskeyCredentialsByUserGuid() {
        String userGuid = GuidGenerator.generate();

        PasskeyCredential credential1 = new PasskeyCredential()
                .userGuid(userGuid)
                .credentialId(GuidGenerator.generate())
                .publicKey("key-1")
                .publicKeyAlgorithm(-7)
                .signCount(1L)
                .label("device-1");
        PasskeyCredential credential2 = new PasskeyCredential()
                .userGuid(userGuid)
                .credentialId(GuidGenerator.generate())
                .publicKey("key-2")
                .publicKeyAlgorithm(-257)
                .signCount(2L)
                .label("device-2");

        passkeyCredentialRepository.savePasskeyCredential(credential1);
        passkeyCredentialRepository.savePasskeyCredential(credential2);

        List<PasskeyCredential> list = passkeyCredentialRepository.findPasskeyCredentialsByUserGuid(userGuid);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void updatePasskeyCredentialCounter() {
        String credentialId = GuidGenerator.generate();

        PasskeyCredential credential = new PasskeyCredential()
                .userGuid(GuidGenerator.generate())
                .credentialId(credentialId)
                .publicKey("test-public-key")
                .publicKeyAlgorithm(-7)
                .signCount(3L)
                .label("device-update");
        passkeyCredentialRepository.savePasskeyCredential(credential);

        passkeyCredentialRepository.updatePasskeyCredentialCounter(credentialId, 10L);

        PasskeyCredential result = passkeyCredentialRepository.findPasskeyCredential(credentialId);
        assertNotNull(result);
        assertEquals(10L, result.signCount());
        assertNotNull(result.lastUsedTime());
    }

    @Test
    public void deletePasskeyCredential() {
        String userGuid = GuidGenerator.generate();
        String credentialId = GuidGenerator.generate();

        PasskeyCredential credential = new PasskeyCredential()
                .userGuid(userGuid)
                .credentialId(credentialId)
                .publicKey("test-public-key")
                .publicKeyAlgorithm(-7)
                .signCount(3L)
                .label("device-delete");
        passkeyCredentialRepository.savePasskeyCredential(credential);

        PasskeyCredential result = passkeyCredentialRepository.findPasskeyCredential(credentialId);
        assertNotNull(result);

        passkeyCredentialRepository.deletePasskeyCredential(credentialId, userGuid);

        result = passkeyCredentialRepository.findPasskeyCredential(credentialId);
        assertNull(result);
    }
}
