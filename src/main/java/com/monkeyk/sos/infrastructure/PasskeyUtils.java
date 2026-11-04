package com.monkeyk.sos.infrastructure;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Passkey/WebAuthn 工具类。
 * <p>
 * 负责 challenge 生成、Base64URL 编解码、
 * clientData/authenticatorData 解析以及签名校验。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public final class PasskeyUtils {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    /**
     * 工具类不允许实例化。
     */
    private PasskeyUtils() {
    }

    /**
     * 生成一次性的 WebAuthn challenge。
     */
    public static String newChallenge() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return base64UrlEncode(bytes);
    }

    /**
     * 使用 Base64URL 对字节数组进行编码。
     */
    public static String base64UrlEncode(byte[] bytes) {
        return URL_ENCODER.encodeToString(bytes);
    }

    /**
     * 将 Base64URL 字符串解码为字节数组。
     */
    public static byte[] base64UrlDecode(String value) {
        return URL_DECODER.decode(value);
    }

    /**
     * 以 UTF-8 编码将字符串转为字节数组。
     */
    public static byte[] utf8Bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 解析浏览器返回的 clientDataJSON。
     */
    public static ClientData clientData(String clientDataJsonBase64) {
        try {
            byte[] jsonBytes = base64UrlDecode(clientDataJsonBase64);
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonBytes);
            return new ClientData(jsonNode.path("type").asText(), jsonNode.path("challenge").asText(),
                    jsonNode.path("origin").asText(), jsonBytes);
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid clientDataJSON", ex);
        }
    }

    /**
     * 解析 authenticatorData 中的 rpIdHash、flags 与 signCount。
     */
    public static AuthenticatorData authenticatorData(String authenticatorDataBase64) {
        byte[] bytes = base64UrlDecode(authenticatorDataBase64);
        if (bytes.length < 37) {
            throw new IllegalStateException("Invalid authenticatorData");
        }

        byte[] rpIdHash = new byte[32];
        System.arraycopy(bytes, 0, rpIdHash, 0, rpIdHash.length);
        byte flags = bytes[32];
        long signCount = Integer.toUnsignedLong(ByteBuffer.wrap(bytes, 33, 4).getInt());
        return new AuthenticatorData(rpIdHash, flags, signCount, bytes);
    }

    /**
     * 校验注册流程中的关键信息。
     */
    public static void verifyRegistration(String expectedChallenge, String expectedOrigin, String expectedRpId,
                                          String clientDataJsonBase64, String authenticatorDataBase64) {
        ClientData clientData = clientData(clientDataJsonBase64);
        if (!"webauthn.create".equals(clientData.type())) {
            throw new IllegalStateException("Invalid passkey registration type");
        }
        verifyClientData(expectedChallenge, expectedOrigin, expectedRpId, clientData, authenticatorDataBase64);

        AuthenticatorData authenticatorData = authenticatorData(authenticatorDataBase64);
        if (!authenticatorData.userPresent()) {
            throw new IllegalStateException("User presence check failed");
        }
        if (!authenticatorData.hasAttestedCredentialData()) {
            throw new IllegalStateException("Attested credential data is required");
        }
    }

    /**
     * 校验登录流程中的关键信息。
     */
    public static void verifyAuthentication(String expectedChallenge, String expectedOrigin, String expectedRpId,
                                            String clientDataJsonBase64, String authenticatorDataBase64) {
        ClientData clientData = clientData(clientDataJsonBase64);
        if (!"webauthn.get".equals(clientData.type())) {
            throw new IllegalStateException("Invalid passkey authentication type");
        }
        verifyClientData(expectedChallenge, expectedOrigin, expectedRpId, clientData, authenticatorDataBase64);

        AuthenticatorData authenticatorData = authenticatorData(authenticatorDataBase64);
        if (!authenticatorData.userPresent()) {
            throw new IllegalStateException("User presence check failed");
        }
    }

    private static void verifyClientData(String expectedChallenge, String expectedOrigin, String expectedRpId,
                                         ClientData clientData, String authenticatorDataBase64) {
        if (!expectedChallenge.equals(clientData.challenge())) {
            throw new IllegalStateException("Passkey challenge mismatch");
        }
        if (!expectedOrigin.equals(clientData.origin())) {
            throw new IllegalStateException("Passkey origin mismatch");
        }
        AuthenticatorData authenticatorData = authenticatorData(authenticatorDataBase64);
        byte[] expectedRpIdHash = sha256(utf8Bytes(expectedRpId));
        if (!MessageDigest.isEqual(expectedRpIdHash, authenticatorData.rpIdHash())) {
            throw new IllegalStateException("Passkey rpId mismatch");
        }
    }

    /**
     * 使用保存的公钥校验浏览器返回的签名结果。
     */
    public static void verifySignature(String publicKeyBase64, int publicKeyAlgorithm,
                                       String authenticatorDataBase64, String clientDataJsonBase64,
                                       String signatureBase64) {
        try {
            PublicKey publicKey = loadPublicKey(publicKeyBase64, publicKeyAlgorithm);
            Signature signature = Signature.getInstance(signatureAlgorithm(publicKeyAlgorithm));
            signature.initVerify(publicKey);
            signature.update(base64UrlDecode(authenticatorDataBase64));
            signature.update(sha256(base64UrlDecode(clientDataJsonBase64)));
            if (!signature.verify(base64UrlDecode(signatureBase64))) {
                throw new IllegalStateException("Passkey signature verify failed");
            }
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Verify passkey signature failed", ex);
        }
    }

    /**
     * 根据算法编号加载公钥对象。
     */
    public static PublicKey loadPublicKey(String publicKeyBase64, int publicKeyAlgorithm) {
        try {
            byte[] encoded = base64UrlDecode(publicKeyBase64);
            String keyType;
            if (publicKeyAlgorithm == -7) {
                keyType = "EC";
            } else if (publicKeyAlgorithm == -257) {
                keyType = "RSA";
            } else {
                throw new IllegalStateException("Unsupported passkey algorithm: " + publicKeyAlgorithm);
            }
            return KeyFactory.getInstance(keyType).generatePublic(new X509EncodedKeySpec(encoded));
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Load passkey public key failed", ex);
        }
    }

    /**
     * 计算 SHA-256 摘要。
     */
    public static byte[] sha256(byte[] bytes) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(bytes);
        } catch (Exception ex) {
            throw new IllegalStateException("SHA-256 digest failed", ex);
        }
    }

    private static String signatureAlgorithm(int publicKeyAlgorithm) {
        if (publicKeyAlgorithm == -7) {
            return "SHA256withECDSA";
        }
        if (publicKeyAlgorithm == -257) {
            return "SHA256withRSA";
        }
        throw new IllegalStateException("Unsupported passkey algorithm: " + publicKeyAlgorithm);
    }

    /**
     * 浏览器返回的 clientData 数据结构。
     */
    public static class ClientData {

        private final String type;
        private final String challenge;
        private final String origin;
        private final byte[] bytes;

        public ClientData(String type, String challenge, String origin, byte[] bytes) {
            this.type = type;
            this.challenge = challenge;
            this.origin = origin;
            this.bytes = bytes;
        }

        public String type() {
            return type;
        }

        public String challenge() {
            return challenge;
        }

        public String origin() {
            return origin;
        }

        public byte[] bytes() {
            return bytes;
        }
    }

    /**
     * 浏览器返回的 authenticatorData 数据结构。
     */
    public static class AuthenticatorData {

        private final byte[] rpIdHash;
        private final byte flags;
        private final long signCount;
        private final byte[] bytes;

        public AuthenticatorData(byte[] rpIdHash, byte flags, long signCount, byte[] bytes) {
            this.rpIdHash = rpIdHash;
            this.flags = flags;
            this.signCount = signCount;
            this.bytes = bytes;
        }

        public byte[] rpIdHash() {
            return rpIdHash;
        }

        public byte flags() {
            return flags;
        }

        public long signCount() {
            return signCount;
        }

        public byte[] bytes() {
            return bytes;
        }

        public boolean userPresent() {
            return (flags & 0x01) != 0;
        }

        public boolean hasAttestedCredentialData() {
            return (flags & 0x40) != 0;
        }
    }
}
