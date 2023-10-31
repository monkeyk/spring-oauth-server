package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * 2023/10/31 10:27
 *
 * @author Shengzhao Li
 * @see org.springframework.security.web.authentication.AuthenticationConverter
 * @since 3.0.0
 */
public interface AuthenticationRestConverter {

    /**
     * 从请求参数中转化到  Authentication
     *
     * @param parameters 请求参数
     * @return Authentication or null
     */
    Authentication convert(Map<String, String> parameters);

}
