package com.monkeyk.sos.web.controller.resource;

import com.monkeyk.sos.service.OauthService;
import com.monkeyk.sos.service.UserService;
import com.monkeyk.sos.service.dto.UserJsonDto;
import com.monkeyk.sos.web.controller.OauthClientDetailsDtoValidator;
import com.monkeyk.sos.web.controller.UserFormDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 2023/10/19 17:31
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
@WebMvcTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UnityControllerTest {


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
    void userInfo() throws Exception {


        UserJsonDto jsonDto = new UserJsonDto();
        String username = "user111";
        jsonDto.setUsername(username);
        jsonDto.setGuid("owwiwi0a0assdfsfs11");
        jsonDto.setEmail("user111@cloudjac.com");
        jsonDto.setPhone("13300002222");
        jsonDto.getPrivileges().add("ROLE_USER");

        Mockito.when(userService.loadCurrentUserJsonDto()).thenReturn(jsonDto);


        MockHttpServletRequestBuilder requestBuilder = get("/unity/user_info")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("guid").exists())
                .andExpect(jsonPath("username").value(username))
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("phone").exists())
                //生成文档需要加上这句
                .andDo(document("{ClassName}/{methodName}"));


    }


}