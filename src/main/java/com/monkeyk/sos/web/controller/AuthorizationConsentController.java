
package com.monkeyk.sos.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

import static com.monkeyk.sos.domain.shared.SOSConstants.AUTHORIZATION_ENDPOINT_URI;
import static com.monkeyk.sos.domain.shared.SOSConstants.DEVICE_VERIFICATION_ENDPOINT_URI;


/**
 * 2023/10/18
 * <p>
 * consent flow
 *
 * @author shengzhao Li
 * @since 3.0.0
 */
@Controller
public class AuthorizationConsentController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationConsentController.class);

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private OAuth2AuthorizationConsentService authorizationConsentService;


    /**
     * 扩展，自定义的 consent
     */
    @GetMapping(value = "/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        //再次检查 client
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Not found RegisteredClient by clientId: {}", clientId);
            }
            model.addAttribute("error", "Invalid client_id: " + clientId);
            return "consent_error";
        }

        // Remove scopes that were already approved
        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();

        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());
        Set<String> authorizedScopes;
        if (currentAuthorizationConsent != null) {
            authorizedScopes = currentAuthorizationConsent.getScopes();
        } else {
            authorizedScopes = Collections.emptySet();
        }
        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userCode", userCode);
        if (StringUtils.hasText(userCode)) {
            model.addAttribute("requestURI", DEVICE_VERIFICATION_ENDPOINT_URI);
        } else {
            model.addAttribute("requestURI", AUTHORIZATION_ENDPOINT_URI);
        }

        return "consent";
    }

    private static Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        Set<ScopeWithDescription> scopeWithDescriptions = new HashSet<>();
        for (String scope : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(scope));

        }
        return scopeWithDescriptions;
    }

    public static class ScopeWithDescription {
        private static final String DEFAULT_DESCRIPTION
                = "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";
        private static final Map<String, String> SCOPE_DESCRIPTIONS = new HashMap<>();

        static {
            SCOPE_DESCRIPTIONS.put(
                    OidcScopes.PROFILE,
                    "This application will be able to read your profile information."
            );
            SCOPE_DESCRIPTIONS.put(
                    OidcScopes.EMAIL,
                    "This application will be able to read your email information."
            );
            SCOPE_DESCRIPTIONS.put(
                    OidcScopes.PHONE,
                    "This application will be able to read your phone information."
            );
            SCOPE_DESCRIPTIONS.put(
                    OidcScopes.ADDRESS,
                    "This application will be able to read your address information."
            );
            SCOPE_DESCRIPTIONS.put(
                    "message.read",
                    "This application will be able to read your message."
            );
            SCOPE_DESCRIPTIONS.put(
                    "message.write",
                    "This application will be able to add new messages. It will also be able to edit and delete existing messages."
            );
            SCOPE_DESCRIPTIONS.put(
                    "other.scope",
                    "This is another scope example of a scope description."
            );
        }

        public final String scope;
        public final String description;

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = SCOPE_DESCRIPTIONS.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ScopeWithDescription that = (ScopeWithDescription) o;
            return Objects.equals(scope, that.scope) && Objects.equals(description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scope, description);
        }
    }

}
