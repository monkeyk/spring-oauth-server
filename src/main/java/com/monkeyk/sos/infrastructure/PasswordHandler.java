package com.monkeyk.sos.infrastructure;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 2016/3/25
 *
 * @author Shengzhao Li
 */
public abstract class PasswordHandler {


    private PasswordHandler() {
    }


    public static String md5(String password) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(password, null);
    }
}
