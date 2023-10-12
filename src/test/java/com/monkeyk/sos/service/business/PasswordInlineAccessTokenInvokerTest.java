package com.monkeyk.sos.service.business;

import com.monkeyk.sos.service.dto.AccessTokenDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 2019/7/6
 *
 * @author Shengzhao Li
 */
public class PasswordInlineAccessTokenInvokerTest extends AbstractInlineAccessTokenInvokerTest {


    @Test
    @Disabled
    public void invokeNormal() {

        createClientDetails();

        createUser();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "password");
        params.put("scope", "read");
        params.put("username", username);
        params.put("password", password);


        PasswordInlineAccessTokenInvoker accessTokenInvoker = new PasswordInlineAccessTokenInvoker();
        final AccessTokenDto tokenDto = accessTokenInvoker.invoke(params);

        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());

//        System.out.println(accessTokenDto);

    }


    @Test()
    public void invalidUsername() {

        createClientDetails();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "password");
        params.put("scope", "read");

        params.put("username", "useraaa");
        params.put("password", "password");

        PasswordInlineAccessTokenInvoker accessTokenInvoker = new PasswordInlineAccessTokenInvoker();
        assertThrows(Exception.class, () -> {
            accessTokenInvoker.invoke(params);
        });
//        final AccessTokenDto tokenDto = accessTokenInvoker.invoke(params);

//        assertNull(tokenDto);

//        System.out.println(accessTokenDto);

    }


    @Test()
    public void invalidScope() {

        createClientDetails();
        createUser();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "password");
//        params.put("scope", "read");

        params.put("username", username);
        params.put("password", password);

        PasswordInlineAccessTokenInvoker accessTokenInvoker = new PasswordInlineAccessTokenInvoker();
        assertThrows(IllegalStateException.class, () -> {
            accessTokenInvoker.invoke(params);
        });
//        final AccessTokenDto tokenDto = accessTokenInvoker.invoke(params);

//        assertNull(tokenDto);

//        System.out.println(accessTokenDto);

    }


}