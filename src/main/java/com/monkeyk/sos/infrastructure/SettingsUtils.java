package com.monkeyk.sos.infrastructure;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.server.authorization.jackson.OAuth2AuthorizationServerJacksonModule;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Map;

/**
 * 2023/10/13 14:49
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public abstract class SettingsUtils {


    // Jackson 3: 包名 com.fasterxml.jackson.* -> tools.jackson.*；
    // SecurityJackson2Modules -> SecurityJacksonModules，OAuth2AuthorizationServerJackson2Module -> OAuth2AuthorizationServerJacksonModule
    private static final ObjectMapper objectMapper = JsonMapper.builder()
//            ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
            .addModules(SecurityJacksonModules.getModules(SettingsUtils.class.getClassLoader()))
            .addModule(new OAuth2AuthorizationServerJacksonModule())
            .build();


    private SettingsUtils() {
    }

    /**
     * text settings ->  TokenSettings
     *
     * @param settings text
     * @return TokenSettings
     */
    public static TokenSettings buildTokenSettings(String settings) {
        Map<String, Object> map = parseMap(settings);
        TokenSettings.Builder builder = TokenSettings.withSettings(map);
        if (!map.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            builder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        return builder.build();
    }

    /**
     * TokenSettings -> text
     *
     * @param settings TokenSettings
     * @return text
     */
    public static String textTokenSettings(TokenSettings settings) {
        Map<String, Object> map = settings.getSettings();
        return writeMap(map);
    }


    /**
     * ClientSettings -> text
     *
     * @param settings ClientSettings
     * @return text
     */
    public static String textClientSettings(ClientSettings settings) {
        Map<String, Object> map = settings.getSettings();
        return writeMap(map);
    }


    /**
     * text settings ->  ClientSettings
     *
     * @param settings text
     * @return ClientSettings
     */
    public static ClientSettings buildClientSettings(String settings) {
        Map<String, Object> map = parseMap(settings);
        return ClientSettings.withSettings(map)
                .build();
    }


    private static Map<String, Object> parseMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private static String writeMap(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
