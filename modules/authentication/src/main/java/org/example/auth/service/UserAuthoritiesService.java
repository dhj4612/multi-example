package org.example.auth.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.auth.constant.UserAuthoritiesState;
import org.example.auth.model.entity.UserAuthorities;
import org.example.core.base.BaseEntity;

/**
 * @author dhj
 * @since 2024-05-11
 */
public interface UserAuthoritiesService extends IService<UserAuthorities> {
    default LambdaQueryChainWrapper<UserAuthorities> normalCondition() {
        return lambdaQuery()
                .eq(UserAuthorities::getState, UserAuthoritiesState.Enable)
                .eq(BaseEntity::getDeleted, 0);
    }
}
