package com.monkeyk.sos.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/oauth2/jwt_bearer/jwks")
    public void jwks() {

    }

}
