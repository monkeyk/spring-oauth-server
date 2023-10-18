package com.monkeyk.sos.infrastructure;


import com.monkeyk.sos.ContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * @author Shengzhao Li
 */
public class PasswordHandlerTest extends ContextTest {


    @Autowired
    private PasswordEncoder passwordEncoder;


//    @Test
//    public void testMd5() throws Exception {
//
//        final String md5 = PasswordHandler.encode("123456");
//        assertNotNull(md5);
////        System.out.println(md5);
//    }

    @Test
    void encode() throws Exception {

        String pwd = "Admin@2013";
        String encode = PasswordHandler.encode(pwd);
        assertNotNull(encode);
//        System.out.println(encode);

    }

    @Test
    void matches() {
        String pwd = "Admin@2013";
        boolean matches = passwordEncoder.matches(pwd, "$2a$10$bIIt6KqIMweTZZC.IIHBLuN3dEIJL0LQFRPrtWTujn9O3Sl5Us5vW");
        assertTrue(matches);
    }

}