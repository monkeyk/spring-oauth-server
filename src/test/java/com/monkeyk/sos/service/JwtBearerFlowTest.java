package com.monkeyk.sos.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 2023/10/24 10:25
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public class JwtBearerFlowTest {


    /**
     * MAC 生成 assertion
     * HS256
     * method:  CLIENT_SECRET_JWT
     *
     * @throws Exception e
     */
    @Test
    void macAssertion() throws Exception {

        String clientId = "vLIXDF9GXg6Psfh1uzwVFUj0fucX2Zn9";
        // client_secret 加密后的值
        String macSecret = "$2a$10$kjjdfA8SIuhlVx0q4B1GYeU..9TNU9.Aj6Vdc2v/iQTJhhmT/0xCi";

        JWSSigner jwsSigner = new MACSigner(macSecret);

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);


        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(clientId)
                .issuer(clientId)
                .audience("http://127.0.0.1:8080")
                .expirationTime(Date.from(Instant.now().plusSeconds(300L)))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        //签名
        jwsObject.sign(jwsSigner);

        // 将 assertion 复制放到请求参数  client_assertion 的值
        String assertion = jwsObject.serialize();
        assertNotNull(assertion);
//        System.out.println(assertion);

    }


    /**
     * RSA 生成 assertion
     * SignatureAlgorithm:  RS256
     * method:  PRIVATE_KEY_JWT
     *
     * @throws Exception e
     */
    void rs256Assertion() throws Exception {




    }

}
