package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录页专用的 Passkey 请求体。
 * <p>
 * 在基础登录请求上额外带上用户名。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PasskeyLoginRequestDto extends PasskeyAuthenticationRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5852065359060615978L;

    /**
     * 当前正在登录的用户名。
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
