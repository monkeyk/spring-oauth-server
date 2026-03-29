package com.monkeyk.sos.infrastructure;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * PasskeyUtils 单元测试。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
class PasskeyUtilsTest {

    /**
     * 验证登录流程中的 challenge、rpId 与签名校验逻辑。
     */
    @Test
    void verifyAuthenticationAndSignature() throws Exception {
        String rpId = "127.0.0.1";
        String origin = "http://127.0.0.1:8080";
        String challenge = "test-passkey-challenge";

        byte[] clientDataBytes = ("{" +
                "\"type\":\"webauthn.get\"," +
                "\"challenge\":\"" + challenge + "\"," +
                "\"origin\":\"" + origin + "\"}").getBytes(StandardCharsets.UTF_8);
        String clientDataJson = PasskeyUtils.base64UrlEncode(clientDataBytes);

        byte[] authenticatorDataBytes = authenticatorData(rpId, (byte) 0x01, 9L);
        String authenticatorData = PasskeyUtils.base64UrlEncode(authenticatorDataBytes);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(authenticatorDataBytes);
        signature.update(PasskeyUtils.sha256(clientDataBytes));
        String signatureValue = PasskeyUtils.base64UrlEncode(signature.sign());

        String publicKey = PasskeyUtils.base64UrlEncode(keyPair.getPublic().getEncoded());

        PasskeyUtils.verifyAuthentication(challenge, origin, rpId, clientDataJson, authenticatorData);
        PasskeyUtils.verifySignature(publicKey, -7, authenticatorData, clientDataJson, signatureValue);

        assertEquals(9L, PasskeyUtils.authenticatorData(authenticatorData).signCount());
    }

    /**
     * 验证注册流程中 challenge 不匹配时会抛出异常。
     */
    @Test
    void verifyRegistrationChallengeMismatch() {
        String rpId = "127.0.0.1";
        String origin = "http://127.0.0.1:8080";
        String clientDataJson = PasskeyUtils.base64UrlEncode(("{" +
                "\"type\":\"webauthn.create\"," +
                "\"challenge\":\"another\"," +
                "\"origin\":\"" + origin + "\"}").getBytes(StandardCharsets.UTF_8));
        String authenticatorData = PasskeyUtils.base64UrlEncode(authenticatorData(rpId, (byte) 0x41, 1L));

        assertThrows(IllegalStateException.class, () ->
                PasskeyUtils.verifyRegistration("expected", origin, rpId, clientDataJson, authenticatorData));
    }

    private static byte[] authenticatorData(String rpId, byte flags, long signCount) {
        byte[] rpIdHash = PasskeyUtils.sha256(rpId.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = ByteBuffer.allocate(37);
        buffer.put(rpIdHash);
        buffer.put(flags);
        buffer.putInt((int) signCount);
        return buffer.array();
    }
}
