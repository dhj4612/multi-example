package org.example.auth.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.auth.constant.UserState;
import org.example.auth.model.entity.User;
import org.example.auth.model.param.PhoneCodeLoginParam;
import org.example.auth.model.param.PhonePasswordLoginParam;
import org.example.auth.model.param.PhoneRegisterParam;
import org.example.core.base.BaseEntity;

/**
 * @author dhj
 * @since 2024-05-11
 */
public interface UserService extends IService<User> {

    default LambdaQueryChainWrapper<User> normalCondition() {
        return lambdaQuery()
                .eq(User::getState, UserState.Normal)
                .eq(BaseEntity::getDeleted, 0);
    }

    String phonePasswordLogin(PhonePasswordLoginParam loginParam);

    String phoneCodeLogin(PhoneCodeLoginParam loginParam);

    void phoneRegister(PhoneRegisterParam param);
}
