package com.monkeyk.sos.web.controller;

import com.monkeyk.sos.domain.shared.security.SOSUserDetails;
import com.monkeyk.sos.infrastructure.PasskeyProperties;
import com.monkeyk.sos.infrastructure.PasskeyUtils;
import com.monkeyk.sos.service.PasskeyService;
import com.monkeyk.sos.service.UserService;
import com.monkeyk.sos.service.dto.PasskeyAssertionOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyLoginRequestDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationOptionsDto;
import com.monkeyk.sos.service.dto.PasskeyRegistrationRequestDto;
import com.monkeyk.sos.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Passkey Web 控制器。
 * <p>
 * 提供注册、登录、删除与管理页面相关入口。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Controller
public class PasskeyController {

    private static final Logger LOG = LoggerFactory.getLogger(PasskeyController.class);

    /**
     * Session 中保存注册 challenge 的键。
     */
    private static final String PASSKEY_REGISTRATION_CHALLENGE = "PASSKEY_REGISTRATION_CHALLENGE";

    /**
     * Session 中保存登录 challenge 的键。
     */
    private static final String PASSKEY_AUTHENTICATION_CHALLENGE = "PASSKEY_AUTHENTICATION_CHALLENGE";

    /**
     * Session 中保存本次 Passkey 登录用户名的键。
     */
    private static final String PASSKEY_AUTHENTICATION_USERNAME = "PASSKEY_AUTHENTICATION_USERNAME";

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private PasskeyService passkeyService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasskeyProperties passkeyProperties;

    /**
     * Passkey 管理页。
     */
    @GetMapping("/passkeys")
    public String passkeys(Model model) {
        model.addAttribute("passkeys", passkeyService.loadCurrentUserPasskeys());
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Go to passkeys management", WebUtils.getIp());
        }
        return "passkeys/manage";
    }

    /**
     * 生成当前登录用户的 Passkey 注册参数。
     */
    @PostMapping("/webauthn/registration/options")
    @ResponseBody
    public PasskeyRegistrationOptionsDto registrationOptions(Authentication authentication, HttpServletRequest request) {
        SOSUserDetails userDetails = (SOSUserDetails) authentication.getPrincipal();
        String challenge = PasskeyUtils.newChallenge();
        request.getSession(true).setAttribute(PASSKEY_REGISTRATION_CHALLENGE, challenge);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Prepare Passkey registration options for username: {}", WebUtils.getIp(), userDetails.getUsername());
        }

        return passkeyService.prepareRegistrationOptions(userDetails.getUsername(), challenge,
                passkeyProperties.resolveRpId(request), passkeyProperties.resolveRpName());
    }

    /**
     * 校验并保存当前登录用户的 Passkey。
     */
    @PostMapping("/webauthn/registration/verify")
    @ResponseBody
    public Map<String, Object> verifyRegistration(Authentication authentication,
                                                  @RequestBody PasskeyRegistrationRequestDto requestDto,
                                                  HttpServletRequest request) {
        SOSUserDetails userDetails = (SOSUserDetails) authentication.getPrincipal();
        String challenge = sessionAttribute(request, PASSKEY_REGISTRATION_CHALLENGE);
        passkeyService.registerPasskey(userDetails.getUsername(), requestDto, challenge,
                passkeyProperties.resolveOrigin(request), passkeyProperties.resolveRpId(request));
        request.getSession(false).removeAttribute(PASSKEY_REGISTRATION_CHALLENGE);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Verify Passkey registration success for username: {}", WebUtils.getIp(), userDetails.getUsername());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("message", "Passkey registered successfully");
        return result;
    }

    /**
     * 根据用户名生成登录页使用的 Passkey options。
     */
    @PostMapping("/webauthn/authentication/options")
    @ResponseBody
    public PasskeyAssertionOptionsDto authenticationOptions(@RequestBody Map<String, String> requestBody,
                                                            HttpServletRequest request) {
        String username = requestBody.get("username");
        if (StringUtils.isBlank(username)) {
            LOG.warn("{}|Username is required for passkey login", WebUtils.getIp());
            throw new IllegalStateException("Username is required for passkey login");
        }
        String challenge = PasskeyUtils.newChallenge();
        request.getSession(true).setAttribute(PASSKEY_AUTHENTICATION_CHALLENGE, challenge);
        request.getSession(true).setAttribute(PASSKEY_AUTHENTICATION_USERNAME, username);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Prepare Passkey authentication options for username: {}", WebUtils.getIp(), username);
        }
        return passkeyService.prepareAssertionOptions(username, challenge, passkeyProperties.resolveRpId(request));
    }

    /**
     * 校验 Passkey 登录结果并写入 Spring Security 会话。
     */
    @PostMapping("/webauthn/authentication/verify")
    @ResponseBody
    public Map<String, Object> verifyAuthentication(@RequestBody PasskeyLoginRequestDto requestDto,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String username = requestDto.getUsername();
        if (StringUtils.isBlank(username)) {
            LOG.warn("{}|Username is required for passkey login", WebUtils.getIp());
            throw new IllegalStateException("Username is required for passkey login");
        }

        String challenge = sessionAttribute(request, PASSKEY_AUTHENTICATION_CHALLENGE);
        String challengeUsername = sessionAttribute(request, PASSKEY_AUTHENTICATION_USERNAME);
        if (!StringUtils.equals(username, challengeUsername)) {
            LOG.warn("{}|Passkey authentication challenge missing for username: {}", WebUtils.getIp(), username);
            throw new IllegalStateException("Passkey authentication challenge is missing");
        }

        String loginUsername = passkeyService.authenticate(username, requestDto, challenge,
                passkeyProperties.resolveOrigin(request), passkeyProperties.resolveRpId(request));
        UserDetails userDetails = userService.loadUserByUsername(loginUsername);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String redirectTo = savedRequest != null ? savedRequest.getRedirectUrl() : "/";
        requestCache.removeRequest(request, response);
        request.getSession(false).removeAttribute(PASSKEY_AUTHENTICATION_CHALLENGE);
        request.getSession(false).removeAttribute(PASSKEY_AUTHENTICATION_USERNAME);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Passkey login success for username: {}", WebUtils.getIp(), username);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("redirectTo", redirectTo);
        return result;
    }

    /**
     * 删除当前登录用户的某个 Passkey。
     */
    @PostMapping("/webauthn/passkeys/delete/{credentialId}")
    public String deletePasskey(@PathVariable("credentialId") String credentialId) {
        passkeyService.deleteCurrentUserPasskey(credentialId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}|Delete Passkey by credentialId: {}", WebUtils.getIp(), credentialId);
        }
        return "redirect:/passkeys";
    }

    /**
     * 统一处理 Passkey 业务异常。
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public Map<String, Object> handleIllegalState(IllegalStateException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        LOG.warn("{}|Passkey request failed: {}", WebUtils.getIp(), ex.getMessage());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", false);
        result.put("message", ex.getMessage());
        return result;
    }


    /**
     * 读取 Session 中的指定属性。
     */
    private String sessionAttribute(HttpServletRequest request, String key) {
        return request.getSession(false) == null ? null : (String) request.getSession(false).getAttribute(key);
    }
}
