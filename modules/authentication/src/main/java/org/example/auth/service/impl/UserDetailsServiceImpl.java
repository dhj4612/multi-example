package org.example.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auth.model.dto.SecurityUserDTO;
import org.example.auth.model.entity.User;
import org.example.auth.model.entity.UserAuthorities;
import org.example.auth.service.UserAuthoritiesService;
import org.example.auth.service.UserService;
import org.example.core.exception.BizException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final UserAuthoritiesService userAuthoritiesService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserAuthorities> authoritiesList = userAuthoritiesService.lambdaQuery().eq(UserAuthorities::getIdentifier, username).list();
        UserAuthorities userAuthorities = authoritiesList.stream()
                .findFirst()
                .orElseThrow(() -> new BizException("用户=>" + username + "不存在"));

        User user = userService.getById(userAuthorities.getUserId());
        if (Objects.isNull(user)) {
            throw new BizException("用户=>" + username + "不存在");
        }
        return getSecurityUser(user, userAuthorities);
    }

    private static SecurityUserDTO getSecurityUser(User user, UserAuthorities userAuthorities) {
        SecurityUserDTO securityUser = new SecurityUserDTO();
        securityUser.setId(user.getId());
        securityUser.setUsername(userAuthorities.getIdentifier());
        securityUser.setState(user.getState());
        securityUser.setAuthorities(Collections.emptySet());
        securityUser.setPassword(userAuthorities.getCredential());
        securityUser.setNickName(user.getNickname());
        return securityUser;
    }
}
