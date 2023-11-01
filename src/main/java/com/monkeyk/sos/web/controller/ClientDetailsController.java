package com.monkeyk.sos.web.controller;

import com.monkeyk.sos.infrastructure.PKCEUtils;
import com.monkeyk.sos.service.dto.OauthClientDetailsDto;
import com.monkeyk.sos.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handle 'client_details' management
 * <p>
 * v3.0.0 中叫 'RegisteredClient', table: oauth2_registered_client
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.client.RegisteredClient
 */
@Controller
public class ClientDetailsController {


    @Autowired
    private OauthService oauthService;

    @Autowired
    private OauthClientDetailsDtoValidator clientDetailsDtoValidator;


    @RequestMapping("client_details")
    public String clientDetails(Model model) {
        List<OauthClientDetailsDto> clientDetailsDtoList = oauthService.loadAllOauthClientDetailsDtos();
        model.addAttribute("clientDetailsDtoList", clientDetailsDtoList);
        return "clientdetails/client_details";
    }


    /**
     * Logic delete
     */
    @RequestMapping("archive_client/{clientId}")
    public String archiveClient(@PathVariable("clientId") String clientId) {
        oauthService.archiveOauthClientDetails(clientId);
        return "redirect:../client_details";
    }

    /**
     * Test client
     */
    @GetMapping("test_client/{clientId}")
    public String testClient(@PathVariable("clientId") String clientId, Model model) {
        OauthClientDetailsDto clientDetailsDto = oauthService.loadOauthClientDetailsDto(clientId);
        model.addAttribute("clientDetailsDto", clientDetailsDto);
        //v3.0.0 added  PKCE params
        String codeVerifier = PKCEUtils.generateCodeVerifier();
        String codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier);
        model.addAttribute("codeVerifier", codeVerifier)
                .addAttribute("codeChallenge", codeChallenge);
        return "clientdetails/test_client";
    }


    /**
     * Register client
     */
    @RequestMapping(value = "register_client", method = RequestMethod.GET)
    public String registerClient(Model model) {
        OauthClientDetailsDto formDto = new OauthClientDetailsDto();
        //初始化 v3.0.0 added
        formDto.setClientAuthenticationMethods("client_secret_post");
        formDto.setScopes(OidcScopes.OPENID);
        formDto.setAuthorizationGrantTypes(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());

        model.addAttribute("formDto", formDto);
        return "clientdetails/register_client";
    }


    /**
     * Submit register client
     */
    @RequestMapping(value = "register_client", method = RequestMethod.POST)
    public String submitRegisterClient(@ModelAttribute("formDto") OauthClientDetailsDto formDto, BindingResult result) {
        clientDetailsDtoValidator.validate(formDto, result);
        if (result.hasErrors()) {
            return "clientdetails/register_client";
        }
        oauthService.registerClientDetails(formDto);
        return "redirect:client_details";
    }


}