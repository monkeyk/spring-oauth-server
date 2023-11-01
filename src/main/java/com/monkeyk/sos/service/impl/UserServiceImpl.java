package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.domain.shared.security.SOSUserDetails;
import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.domain.user.UserRepository;
import com.monkeyk.sos.service.UserService;
import com.monkeyk.sos.service.dto.UserDto;
import com.monkeyk.sos.service.dto.UserFormDto;
import com.monkeyk.sos.service.dto.UserJsonDto;
import com.monkeyk.sos.service.dto.UserOverviewDto;
import com.monkeyk.sos.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shengzhao Li
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;

    /**
     * 提示：产品化的设计此处应加上缓存提高性能
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null || user.archived()) {
            throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
        }

        return new SOSUserDetails(user);
    }

    @Override
    public UserJsonDto loadCurrentUserJsonDto() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();

//        if (authentication instanceof OAuth2Authentication &&
//                (principal instanceof String || principal instanceof org.springframework.security.core.userdetails.User)) {
//            return loadOauthUserJsonDto((OAuth2Authentication) authentication);
//        } else {
        final SOSUserDetails userDetails = (SOSUserDetails) principal;
        return new UserJsonDto(userRepository.findByGuid(userDetails.getUserGuid()));
//        }
    }

    /**
     * 提示：产品化的设计此处应该有分页会更好
     */
    @Override
    public UserOverviewDto loadUserOverviewDto(UserOverviewDto overviewDto) {
        List<User> users = userRepository.findUsersByUsername(overviewDto.getUsername());
        overviewDto.setUserDtos(UserDto.toDtos(users));
        return overviewDto;
    }

    @Override
    public boolean isExistedUsername(String username) {
        final User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public String saveUser(UserFormDto formDto) {
        User user = formDto.newUser();
        userRepository.saveUser(user);
        LOG.debug("{}|Save User: {}", WebUtils.getIp(), user);
        return user.guid();
    }


//    private UserJsonDto loadOauthUserJsonDto(OAuth2Authentication oAuth2Authentication) {
//        UserJsonDto userJsonDto = new UserJsonDto();
//        userJsonDto.setUsername(oAuth2Authentication.getName());
//
//        final Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            userJsonDto.getPrivileges().add(authority.getAuthority());
//        }
//
//        return userJsonDto;
//    }
}