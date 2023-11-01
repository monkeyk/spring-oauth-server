package com.monkeyk.sos.web.controller;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 2023/10/24 16:24
 * <p>
 * grant_type=jwt-bearer 中的 jwkSetUrl 实现参考
 * <p>
 * todo: 此实现仅供参考；实际生产时应该由client端应用提供
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
@RestController
public class JwtBearerJwksController {

    private static final Logger LOG = LoggerFactory.getLogger(JwtBearerJwksController.class);


    /**
     * RS256 公私钥对
     * 如何生成? 详见 JwksTest.java
     */
    public static final String RS256_KEY = "{\"p\":\"-Y5ymP0tAtOmpksf6y1rT-CsGUyklercT0vY0fMbkUyZH8igxUr0ZjXVr3Yzhlh8sJ5y5-0IEpPw7L4v7_OmCC-7t_M-ntf2-36rqIrK7AMhGf4mle4pMQhBeIJN0n91wMxmNXMwto4L3MWZ8f6K1QH1cirj3_BQsA4XXEgMMKE\",\"kty\":\"RSA\",\"q\":\"_HUwOfykJSjDkisyAK3QaNDFxik3HLTr7m0kU3UNLc1KRaNTIwPYuLaskGE4Se6Idy8TLc7NuEB96VSd9LaGakrDPBwh9ZcN8uBJVA162TCA1RUJjwO4k33uxkVo8gvNQ5ooBnEdT-rMhrjZa3ko-vLR5KCQHs6Gq6SWLBalth8\",\"d\":\"D65_9R01rDFuXc6qJKlNo8-x52jBYtDJJSxFoXW3Znek3fwTX7Le10lNKHf0EEJixnmXumIivl4hFCCBvlc-KP6P_OZZmU9JzC-gezUFdOuhfouMJh6VpbO272nqIfOU8UZJEXCxMSvOqJs-grekSqWMdEZpFytlG6hxNGVEJcy619rPdKL-xUlIliK0M4BItOn24u0Awd4msHyOz9F5UamDa8dnnuRlCJSnqUxBhvMicxP-k4ZXqx_csiVJt5GSkBU2-68T4NYPsTBqUufXsPVbThcoHI6COdWv8dQ5ovNI6P02aEUYA0-QlGVC4mPCmxo81Q8ukK5UUOvjFP7cAQ\",\"e\":\"AQAB\",\"kid\":\"jwt-bearer-demo-rsa-kid1\",\"key_ops\":[\"encrypt\",\"verify\",\"deriveKey\",\"sign\",\"decrypt\"],\"qi\":\"glJKxfNKRauPqt-yQBuiF6XyfIxSSts0ZZJRyf4CAvlXmruWlZdd2IwY4V67SPBvoOHm1o32zI0clQabPt1ovHS1fMfPuy1L2ytQUL3yVSVddhkG9otadaPQW8kuc86wLdKwUjpBREQjwNeaTxkuoJVPcbXlNsayA6h17ljceBc\",\"dp\":\"lXGWcsN6Ru0UKRVn4d_rGYSDywq4rQZeNCZJi0C4S4TBVeVBUaSXQvYOJurz5AntcZ8RVI3_fZCWgE9MSbdwwApFsdy6rUjLIMQ0a9PhvQAKvJQT60kZ5cD54_60N9AYZgKBWpTGoSvjMqwqil5SKUjpARtqJtq0lxl5J8wFcME\",\"alg\":\"RS256\",\"dq\":\"CiaAEOTKiL_x1Q-ti_9xELXMLeJ8V8gicEytGDntlLjbUp91eUPvU8XsfEWcaMSRchFPeRkGhnD5XwdK7orkLqPg46rR5rjzE5_W8u0z0kWz-F1HLBvfMPbwQcKKrKiy0RQCpfeoUQ1Euen2u-58KlLXA5U9FjABlCci7pTehss\",\"n\":\"9hp17DWgdCzJBq8T0hyV5F99-7_NtJu01yL95jZ9UF7bErGdqBtfw6_X5NmI1zMwmsAiksARr5_X7Hr3Gg2EbadLPymYAoGpaIwOZV04hHr_pJmqxNOaQU89_CDz-fmOhRoizZgxKAfWGCW1VLrKMaU3h4gs-G2gT0xQPDpkuXDV7WxYViqfLPhP94Cnk-geCeJpkY9q9BFZGkqW9mYeb2Ut1owlgY-Rfz-RID5gqGjL_AS3DYvvNf9_4eI8v3ahqRKDUXccw_sntEwBs95zWbRXQXBHgIKNIKp4ITnsN7OPc66QlJSpzqSkeOx0fvnCJ5bIh4fViqqLtp0akdFZfw\"}";


    /**
     * ES256 公私钥对
     */
    public static final String ES256_KEY = "{\"kty\":\"EC\",\"d\":\"J6ZIiWeVp4fTXAp5W2w9nw7lACkGaAjOAuLOlrzATDo\",\"crv\":\"P-256\",\"kid\":\"jwt-bearer-demo-ecc-kid\",\"key_ops\":[\"sign\",\"verify\",\"encrypt\",\"deriveKey\",\"decrypt\"],\"x\":\"fJ4RA2IawTPMIWx7bqlYTzrjM8Gl4YQMNRaX4isqeDI\",\"y\":\"sBeszsJArg2sdc1AdrxIyDIgDIVw84KWF27FsnkQenc\",\"alg\":\"ES256\"}";


    /**
     * client 端提供的 jwks 参考实现；
     * 返回 public-key
     *
     * @see org.springframework.security.oauth2.server.authorization.web.NimbusJwkSetEndpointFilter
     */
    @GetMapping("/api/public/oauth2/jwt_bearer/demo_jwks")
    public Map<String, Object> jwks() throws Exception {

        JWK rsJwk = JWK.parse(RS256_KEY);
        JWK esJwk = JWK.parse(ES256_KEY);
        JWKSet jwkSet = new JWKSet(Arrays.asList(rsJwk, esJwk));

        //注意：只返回 publicKey
        return jwkSet.toJSONObject(true);
    }

}
