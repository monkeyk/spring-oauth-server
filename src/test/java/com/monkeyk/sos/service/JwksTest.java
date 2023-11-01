package com.monkeyk.sos.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
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

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(point.toECParameterSpec());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();


        ECKey key = new ECKey.Builder(point, (ECPublicKey) aPublic)
                .privateKey(aPrivate)
                .keyOperations(Set.of(
                        SIGN,
                        VERIFY,
                        ENCRYPT,
                        DECRYPT,
                        DERIVE_KEY))
                // keyId 必须唯一
                .keyID("sos-ecc-kid1")
                .algorithm(JWSAlgorithm.ES256)
                .build();
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

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();


        RSAKey key = new RSAKey.Builder((RSAPublicKey) aPublic)
                .privateKey(aPrivate)
//                .keyUse(KeyUse.SIGNATURE)
                .keyOperations(Set.of(
                        SIGN,
                        VERIFY,
                        ENCRYPT,
                        DECRYPT,
                        DERIVE_KEY))
                .algorithm(JWSAlgorithm.RS256)
                .keyID("sos-rsa-kid2")
                .build();

        assertNotNull(key);
        String json = key.toJSONString();
        assertNotNull(json);
//        System.out.println(json);
    }

}
