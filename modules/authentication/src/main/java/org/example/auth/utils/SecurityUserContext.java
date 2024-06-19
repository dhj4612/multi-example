package org.example.auth.utils;

import org.example.auth.model.dto.SecurityUserDTO;
import org.example.core.exception.BizException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUserContext {

    public static Integer getUserId() {
        SecurityUserDTO user = getUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public static Integer ensureGetUserId() {
        SecurityUserDTO securityUser =  ensureGetUser();
        return securityUser.getId();
    }

    public static SecurityUserDTO getUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUserDTO user) {
            return user;
        }
        return null;
    }

    public static SecurityUserDTO ensureGetUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUserDTO user) {
            return user;
        }
        throw new BizException("当前登录用户不存在");
    }
}
