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
 * JWK
 * generate
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
        //key 使用范围请根据业务场景设置, 范围越小越好
        ecKeyGenerator.keyOperations(Set.of(
                        SIGN,
                        VERIFY,
                        ENCRYPT,
                        DECRYPT,
                        DERIVE_KEY))
                // keyId 必须唯一
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
        // keyId 必须唯一
        rsaKeyGenerator.keyID("sos-rsa-kid2")
                .algorithm(JWSAlgorithm.RS256)
                //key 使用范围请根据业务场景设置, 范围越小越好
                .keyOperations(Set.of(
                        SIGN,
                        VERIFY,
                        ENCRYPT,
                        DECRYPT,
                        DERIVE_KEY));

        RSAKey key = rsaKeyGenerator.generate();
        assertNotNull(key);
        String json = key.toJSONString();
        assertNotNull(json);
//        System.out.println(json);
    }

}
