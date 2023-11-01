/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.monkeyk.sos.service.dto;

import com.monkeyk.sos.domain.user.Privilege;
import com.monkeyk.sos.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 2016/3/12
 *
 * @author Shengzhao Li
 */
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2502329463915439215L;


    private String guid;

    private String username;

    private String phone;
    private String email;

    private String createTime;

    private List<Privilege> privileges = new ArrayList<>();

    /**
     * true 启用
     * false 禁用
     *
     * @since 3.0.0
     */
    private boolean enabled = true;

    /**
     * 别名
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#PROFILE
     * @since 3.0.0
     */
    private String nickname;

    /**
     * 地址
     *
     * @see org.springframework.security.oauth2.core.oidc.OidcScopes#ADDRESS
     * @since 3.0.0
     */
    private String address;


    public UserDto() {
    }


    public UserDto(User user) {
        this.guid = user.guid();
        this.username = user.username();
        this.phone = user.phone();
        this.email = user.email();

        this.privileges = user.privileges();
        this.createTime = user.createTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        this.enabled = user.enabled();
        this.address = user.address();
        this.nickname = user.nickname();
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public static List<UserDto> toDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>(users.size());
        for (User user : users) {
            dtos.add(new UserDto(user));
        }
        return dtos;
    }
}
