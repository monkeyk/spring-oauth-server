package com.monkeyk.sos.domain.shared.security;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monkeyk.sos.domain.user.Privilege;
import com.monkeyk.sos.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;

/**
 * @author Shengzhao Li
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class SOSUserDetails extends org.springframework.security.core.userdetails.User {

    @Serial
    private static final long serialVersionUID = 3957586021470480642L;

    public static final String ROLE_PREFIX = "ROLE_";

    public static final GrantedAuthority DEFAULT_USER_ROLE = new SimpleGrantedAuthority(ROLE_PREFIX + Privilege.USER.name());

    /**
     * @since 3.0.0
     */
    protected String userGuid;


    public SOSUserDetails(User user) {
        super(user.username(), user.password(), user.enabled(),
                true, true, true, user.generateAuthorities());
        this.userGuid = user.guid();
    }

    public String getUserGuid() {
        return userGuid;
    }

}