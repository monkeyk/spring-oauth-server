package com.monkeyk.sos.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.nimbusds.jose.jwk.KeyOperation.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 2023/10/18 15:12
 * <p>
 * JWK generate
 * <p>
 * 如何使用生成的密钥：
 * <ol>
 *   <li>运行测试获取 JSON 输出</li>
 *   <li>将输出写入外部文件（如 /etc/sos/jwks.json），或配置到环境变量 SOS_JWKS_VALUE</li>
 *   <li>在 application.properties 中设置 {@code sos.jwks.file-path} 或 {@code sos.jwks.value}</li>
 *   <li>从源码中删除 src/main/resources/jwks.json</li>
 * </ol>
 * <p>
 * 密钥轮换：生成新 kid 的密钥，在 JWKS 文件中同时保留新旧 key（不同 kid），
 * 确认旧 token 全部过期后，再移除旧 key。
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public class JwksTest {


    /**
     * ES256  jwk  generate
     *
     * @throws Exception e
     */
    @Test
    void jwkEC() throws Exception {

        Curve point = Curve.P_256;
//        Curve point = Curve.P_384;
//        Curve point = Curve.P_521;

        ECKeyGenerator ecKeyGenerator = new ECKeyGenerator(point);
        // 安全最佳实践：key_ops 仅声明实际需要的操作，范围越小越安全
        // OAuth2 Authorization Server 仅使用 SIGN/VERIFY
        ecKeyGenerator.keyOperations(Set.of(
                        SIGN,
                        VERIFY))
                // keyId 必须唯一，轮换时使用新 kid（如 sos-ecc-kid2）
                .keyID("sos-ecc-kid1")
                .algorithm(JWSAlgorithm.ES256);
        ECKey key = ecKeyGenerator.generate();
        assertNotNull(key);

        String json = key.toJSONString();
        assertNotNull(json);
//        System.out.println(json);


    }

    /**
     * RS256  jwk  generate
     *
     * @throws Exception e
     */
    @Test
    void jwkRS() throws Exception {
        //size 至少 1024, 推荐 2048
        RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator(2048);
        // keyId 必须唯一，轮换时使用新 kid（如 sos-rsa-kid3）
        rsaKeyGenerator.keyID("sos-rsa-kid2")
                .algorithm(JWSAlgorithm.RS256)
                // 安全最佳实践：key_ops 仅声明实际需要的操作，范围越小越安全
                // OAuth2 Authorization Server 仅使用 SIGN/VERIFY
                .keyOperations(Set.of(
                        SIGN,
                        VERIFY));

        RSAKey key = rsaKeyGenerator.generate();
        assertNotNull(key);
        String json = key.toJSONString();
        assertNotNull(json);
//        System.out.println(json);
    }

}
