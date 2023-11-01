package com.monkeyk.sos.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 2023/10/16 22:53
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
class PKCEUtilsTest {


    @Test
    void generateCodeVerifier() {

        String verifier = PKCEUtils.generateCodeVerifier();
        assertNotNull(verifier);
        assertTrue(verifier.length() >= 32);
    }


    @Test
    void generateCodeChallenge() {

        String verifier = PKCEUtils.generateCodeVerifier();
        assertNotNull(verifier);

        String challenge = PKCEUtils.generateCodeChallenge(verifier);
        assertNotNull(challenge);

    }


    /**
     * PKCE 需要的参数生成测试
     * code_challenge_method : S256   (alg: SHA-256)  固定值
     * code_verifier :  随机生成且base64 encode的值 （推荐随机值至少32位）
     * code_challenge :   对 code_verifier 使用指定算法进行计算(digest)并base encode的值
     *
     */
    @Test
    void pkceFlow() {

        // 1. 随机生成code_verifier
        String codeVerifier = PKCEUtils.generateCodeVerifier();
//        System.out.println("code_verifier -> " + codeVerifier);

        //2. 按指定算法计算 挑战码 code_challenge
        String codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier);

        assertNotNull(codeChallenge);
//        System.out.println("code_challenge -> " + codeChallenge);
    }


}