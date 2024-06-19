package org.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.auth.constant.AuthenticationRedisKey;
import org.example.auth.constant.UserAuthoritiesState;
import org.example.auth.constant.UserAuthoritiesType;
import org.example.auth.constant.UserState;
import org.example.auth.mapper.UserMapper;
import org.example.auth.model.dto.SecurityUserDTO;
import org.example.auth.model.entity.User;
import org.example.auth.model.entity.UserAuthorities;
import org.example.auth.model.param.PhoneCodeLoginParam;
import org.example.auth.model.param.PhonePasswordLoginParam;
import org.example.auth.model.param.PhoneRegisterParam;
import org.example.auth.service.UserAuthoritiesService;
import org.example.auth.service.UserService;
import org.example.auth.utils.JwtTokenUtil;
import org.example.core.exception.BizException;
import org.example.core.utils.JacksonUtil;
import org.example.message.constant.SMSVerifyCodeType;
import org.example.message.dto.SendOrVerifyOfCodeDTO;
import org.example.message.service.SMSMessageService;
import org.example.mysql.core.DbEncryptHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserAuthoritiesService userAuthoritiesService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SMSMessageService smsMessageService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public String phonePasswordLogin(PhonePasswordLoginParam param) {
        UserAuthorities userAuthorities = userAuthoritiesService.normalCondition()
                .eq(UserAuthorities::getIdentifier, DbEncryptHelper.encrypt(param.phone()))
                .eq(UserAuthorities::getUserType, param.type())
                .eq(UserAuthorities::getIdentityType, UserAuthoritiesType.Phone)
                .one();
        Assert.notNull(userAuthorities, "用户授权不存在或已禁用");
        if (!passwordEncoder.matches(param.password(), userAuthorities.getCredential())) {
            throw new BizException("密码错误");
        }
        User user = normalCondition().eq(User::getId, userAuthorities.getUserId()).one();
        Assert.notNull(user, "用户不存在或已禁用");
        String accessToken = JwtTokenUtil.createAccessToken(JwtTokenUtil.builderUserInfo(user));
        redisTemplate.opsForValue().set(
                AuthenticationRedisKey.getLoginSessionRedisKey(user.getUserType(), accessToken),
                JacksonUtil.toJsonStr(SecurityUserDTO.of(user)),
                1L,
                TimeUnit.DAYS
        );
        return accessToken;
    }

    @Override
    public String phoneCodeLogin(PhoneCodeLoginParam param) {
        return "";
    }

    @Override
    @Transactional
    public void phoneRegister(PhoneRegisterParam param) {
        boolean verify = smsMessageService.verifyCode(
                new SendOrVerifyOfCodeDTO()
                        .setPhone(param.phone())
                        .setCode(param.code())
                        .setType(SMSVerifyCodeType.Register)
                        .setSource(param.userType().name())
        );
        if (verify) {
            boolean exists = userAuthoritiesService.normalCondition()
                    .eq(UserAuthorities::getIdentityType, UserAuthoritiesType.Phone)
                    .eq(UserAuthorities::getUserType, param.userType())
                    .eq(UserAuthorities::getIdentifier, DbEncryptHelper.encrypt(param.phone()))
                    .exists();

            if (exists) {
                throw new BizException("用户授权已存在");
            }
            User user = new User();
            user.setUserType(param.userType());
            user.setState(UserState.Normal);
            save(user);

            UserAuthorities userAuthorities = new UserAuthorities();
            userAuthorities.setUserId(user.getId());
            userAuthorities.setIdentifier(param.phone());
            userAuthorities.setIdentityType(UserAuthoritiesType.Phone);
            userAuthorities.setState(UserAuthoritiesState.Enable);
            userAuthorities.setUserType(param.userType());
            userAuthorities.setCredential(passwordEncoder.encode(param.password()));

            userAuthoritiesService.save(userAuthorities);
            return;
        }
        throw new BizException("验证码错误");
    }
}
