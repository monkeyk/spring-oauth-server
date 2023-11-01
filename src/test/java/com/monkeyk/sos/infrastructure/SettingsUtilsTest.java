package com.monkeyk.sos.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2023/10/13 14:59
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
class SettingsUtilsTest {


    @Test
    void textTokenSettings() {

        TokenSettings settings = TokenSettings.builder()
                .reuseRefreshTokens(false)
                .build();
        String s = SettingsUtils.textTokenSettings(settings);
        assertNotNull(s);
//        System.out.println(s);

        TokenSettings tokenSettings = SettingsUtils.buildTokenSettings(s);
        assertNotNull(tokenSettings);

    }

    @Test
    void textClientSettings() {

        ClientSettings settings = ClientSettings.builder()
                .requireProofKey(true)
                .build();
        String s = SettingsUtils.textClientSettings(settings);
        assertNotNull(s);

//        System.out.println(s);

        ClientSettings clientSettings = SettingsUtils.buildClientSettings(s);
        assertNotNull(clientSettings);

    }

}