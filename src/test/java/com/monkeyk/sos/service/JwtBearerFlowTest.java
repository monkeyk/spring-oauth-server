package com.monkeyk.sos.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 2023/10/24 10:25
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public class JwtBearerFlowTest {


    /**
     * MAC 生成 token
     * HS256
     * method:  CLIENT_SECRET_JWT
     *
     * @throws Exception e
     */
    @Test
    void macToken() throws Exception {

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

        String idToken = jwsObject.serialize();
        assertNotNull(idToken);
        System.out.println(idToken);

    }

}
