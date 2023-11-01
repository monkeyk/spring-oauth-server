package com.monkeyk.sos.infrastructure;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 2023/10/16 22:45
 * <p>
 * PKCE tool:
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public abstract class PKCEUtils {

    private static final String ALG = "SHA-256";


    private PKCEUtils() {
    }

    /**
     * 随机生成32的 code_verifier
     *
     * @return code_verifier
     */
    public static String generateCodeVerifier() {
        // 1. 随机生成code_verifier
        String codeVerifierVal = RandomStringUtils.random(32, true, true);
        //2. 对 code_verifier 进行base64 encode
        return Base64.getEncoder().encodeToString(codeVerifierVal.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据指定的 code_verifier 计算 code_challenge
     *
     * @param codeVerifier code_verifier
     * @return code_challenge
     */
    public static String generateCodeChallenge(String codeVerifier) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("JDK not found alg: '" + ALG + "' ??", e);
        }
        byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }


}
