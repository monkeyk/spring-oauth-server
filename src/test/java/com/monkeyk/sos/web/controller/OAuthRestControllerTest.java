package com.monkeyk.sos.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeyk.sos.service.OauthService;
import com.monkeyk.sos.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 2023/10/19 18:11
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
@WebMvcTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class OAuthRestControllerTest {


    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @MockBean
    private RegisteredClientRepository registeredClientRepository;

    @MockBean
    private OAuth2AuthorizationConsentService consentService;

    @MockBean
    private OauthService oauthService;

    @MockBean
    private OauthClientDetailsDtoValidator oauthClientDetailsDtoValidator;

    @MockBean
    private UserFormDtoValidator userFormDtoValidator;


    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthorizationServerSettings authorizationServerSettings;


    @BeforeEach
    public void setup(WebApplicationContext applicationContext, RestDocumentationContextProvider contextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(documentationConfiguration(contextProvider))
                .alwaysDo(result -> {
                    result.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }


    @Test
    @Disabled
    void postAccessToken() throws Exception {


        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", "clientxxxx");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(parameters);
        assertNotNull(content);

        MockHttpServletRequestBuilder requestBuilder = post("/oauth2/rest_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(requestBuilder)
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("access_token").exists())
//                .andExpect(jsonPath("username").value(username))
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("scope").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("expires_in").exists())
                //生成文档需要加上这句
                .andDo(document("{ClassName}/{methodName}"));


    }

}