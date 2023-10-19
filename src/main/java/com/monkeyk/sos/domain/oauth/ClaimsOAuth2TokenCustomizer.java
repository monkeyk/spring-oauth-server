package com.monkeyk.sos.domain.oauth;

import com.monkeyk.sos.domain.shared.GuidGenerator;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;

/**
 * 2023/10/17
 * <p>
 * 扩展 jwt  id_token  claims 属性生成
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
public class ClaimsOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimsOAuth2TokenCustomizer.class);

    @Autowired
    private UserRepository userRepository;

    public ClaimsOAuth2TokenCustomizer() {
    }

    @Override
    public void customize(JwtEncodingContext context) {

        JwtClaimsSet.Builder claims = context.getClaims();
        //jti
        claims.id(GuidGenerator.generateNumber());

        //根据不同的 scope 与 tokenType添加扩展属性
        OAuth2TokenType tokenType = context.getTokenType();
        if (!OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            //非 id_token 排除
            return;
        }
        OAuth2Authorization authorization = context.getAuthorization();
        if (authorization == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Null OAuth2Authorization, ignore customize");
            }
            return;
        }
        String username = authorization.getPrincipalName();
        User user = userRepository.findProfileByUsername(username);
        boolean nullUser = (user == null);

        Set<String> scopes = context.getAuthorizedScopes();
        if (scopes.contains(OidcScopes.ADDRESS)) {
            String attrVal = nullUser ? null : user.address();
            claims.claim(OidcScopes.ADDRESS, attrVal == null ? "" : attrVal);
        }
        if (scopes.contains(OidcScopes.EMAIL)) {
            String attrVal = nullUser ? null : user.email();
            claims.claim(OidcScopes.EMAIL, attrVal == null ? "" : attrVal);
        }
        if (scopes.contains(OidcScopes.PHONE)) {
            String attrVal = nullUser ? null : user.phone();
            claims.claim(OidcScopes.PHONE, attrVal == null ? "" : attrVal);
        }
        if (scopes.contains(OidcScopes.PROFILE)) {
            String attrVal = nullUser ? null : user.nickname();
            claims.claim("nickname", attrVal == null ? "" : attrVal);
            claims.claim("updated_at", 0);
        }
    }
}
