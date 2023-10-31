package com.monkeyk.sos.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 2023/10/31 10:30
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter
 * @since 3.0.0
 */
public final class DelegatingAuthenticationRestConverter implements AuthenticationRestConverter {

    private final List<AuthenticationRestConverter> converters;

    /**
     * Constructs a {@code DelegatingAuthenticationConverter} using the provided parameters.
     *
     * @param converters a {@code List} of {@link AuthenticationConverter}(s)
     */
    public DelegatingAuthenticationRestConverter(List<AuthenticationRestConverter> converters) {
        Assert.notEmpty(converters, "converters cannot be empty");
        this.converters = Collections.unmodifiableList(new LinkedList<>(converters));
    }


    @Override
    public Authentication convert(Map<String, String> parameters) {
        Assert.notNull(parameters, "parameters cannot be null");
        for (AuthenticationRestConverter converter : this.converters) {
            Authentication authentication = converter.convert(parameters);
            if (authentication != null) {
                return authentication;
            }
        }
        return null;
    }
}
