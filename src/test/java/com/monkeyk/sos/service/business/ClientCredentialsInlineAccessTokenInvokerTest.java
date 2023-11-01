package com.monkeyk.sos.service.business;

import com.monkeyk.sos.service.dto.AccessTokenDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * 2019/7/6
 *
 * @author Shengzhao Li
 */
public class ClientCredentialsInlineAccessTokenInvokerTest extends AbstractInlineAccessTokenInvokerTest {


    @Test
    @Disabled
    public void invokeNormal() {

        createClientDetails();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "client_credentials");
        params.put("scope", "read");


        ClientCredentialsInlineAccessTokenInvoker accessTokenInvoker = new ClientCredentialsInlineAccessTokenInvoker();
        final AccessTokenDto accessTokenDto = accessTokenInvoker.invoke(params);

        assertNotNull(accessTokenDto);
        assertNotNull(accessTokenDto.getAccessToken());

//        System.out.println(accessTokenDto);

    }

    //    @Test(expected = NoSuchClientException.class)
    @Test
    public void invalidClientId() {

        createClientDetails();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId + "ssoso");
        params.put("client_secret", clientSecret);
        params.put("grant_type", "client_credentials");
        params.put("scope", "read");


        ClientCredentialsInlineAccessTokenInvoker accessTokenInvoker = new ClientCredentialsInlineAccessTokenInvoker();
//        AccessTokenDto accessTokenDto;
        assertThrows(Exception.class, () -> {
            accessTokenInvoker.invoke(params);
        });
//        final AccessTokenDto accessTokenDto = accessTokenInvoker.invoke(params);

//        assertNotNull(accessTokenDto);
//        assertNotNull(accessTokenDto.getAccessToken());

//        System.out.println(accessTokenDto);

    }

    @Test()
    @Disabled
    public void invalidClientSecret() {

        createClientDetails();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret + "sooe");
        params.put("grant_type", "client_credentials");
        params.put("scope", "read");


        ClientCredentialsInlineAccessTokenInvoker accessTokenInvoker = new ClientCredentialsInlineAccessTokenInvoker();
        final AccessTokenDto accessTokenDto = accessTokenInvoker.invoke(params);

        assertNotNull(accessTokenDto);
        assertNotNull(accessTokenDto.getAccessToken());

//        System.out.println(accessTokenDto);

    }

}