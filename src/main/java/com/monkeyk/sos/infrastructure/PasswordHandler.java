package com.monkeyk.sos.infrastructure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 2016/3/25
 *
 * @author Shengzhao Li
 */
public abstract class PasswordHandler {


    private PasswordHandler() {
    }


    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
