package com.monkeyk.sos.service.dto;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2023/10/13 14:24
 *
 * @author Shengzhao Li
 */
class TokenSettingsDtoTest {


    @Test
    void toSettings() {


        TokenSettingsDto settingsDto = new TokenSettingsDto();
        TokenSettings tokenSettings = settingsDto.toSettings();
        assertNotNull(tokenSettings);
//        System.out.println(tokenSettings);


    }


}