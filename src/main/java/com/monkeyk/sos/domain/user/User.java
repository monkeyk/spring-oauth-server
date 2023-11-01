package com.monkeyk.sos.domain.user;

import com.monkeyk.sos.domain.AbstractDomain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

import static com.monkeyk.sos.domain.shared.security.SOSUserDetails.DEFAULT_USER_ROLE;
import static com.monkeyk.sos.domain.shared.security.SOSUserDetails.ROLE_PREFIX;

/**
 * table: user_
 *
 * @author Shengzhao Li
 */
public class User extends AbstractDomain {

    @Serial
    private static final long serialVersionUID = -2921689304753120556L;


    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 密码，加密存储
     * BCrypt  加密
     */
    private String password;

    /**
     * 手机
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#PHONE
     */
    private String phone;

    /**
     * 邮箱
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#EMAIL
     */
    private String email;
    /**
     * Default user is initial when create database, do not delete
     */
    private boolean defaultUser = false;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 权限值
     */
    private List<Privilege> privileges = new ArrayList<>();

    /**
     * true 启用
     * false 禁用
     */
    private boolean enabled = true;

    /**
     * 别名
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#PROFILE
     */
    private String nickname;

    /**
     * 地址
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#ADDRESS
     */
    private String address;

    /**
     * 更新时间值
     *
     * @since 3.0.0
     */
    private long updatedAt;

    public User() {
    }

    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public boolean defaultUser() {
        return defaultUser;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String phone() {
        return phone;
    }

    public String email() {
        return email;
    }

    public List<Privilege> privileges() {
        return privileges;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{username='").append(username).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", guid='").append(guid).append('\'');
        sb.append(", defaultUser='").append(defaultUser).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User phone(String phone) {
        this.phone = phone;
        return this;
    }


    public User username(String username) {
        this.username = username;
        return this;
    }


    public Date lastLoginTime() {
        return lastLoginTime;
    }

    public User lastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public User createTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     * @since 3.0.0
     */
    public User enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String nickname() {
        return nickname;
    }

    /**
     * @since 3.0.0
     */
    public User nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public String address() {
        return address;
    }

    /**
     * @since 3.0.0
     */
    public User address(String address) {
        this.address = address;
        return this;
    }

    /**
     * @since 3.0.0
     */
    public long updatedAt() {
        return updatedAt;
    }

    /**
     * @since 3.0.0
     */
    public User updatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * 权限值
     *
     * @return GrantedAuthority set
     * @since 3.0.0
     */
    public Set<GrantedAuthority> generateAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        //Default, everyone include
        authorities.add(DEFAULT_USER_ROLE);

        final List<Privilege> privileges = this.privileges();
        for (Privilege privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + privilege.name()));
        }
        return authorities;
    }


}