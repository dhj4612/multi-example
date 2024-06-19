package org.example.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.model.dto.LoginResponseDTO;
import org.example.auth.model.entity.User;
import org.example.auth.model.param.PhoneCodeLoginParam;
import org.example.auth.model.param.PhonePasswordLoginParam;
import org.example.auth.model.param.PhoneRegisterParam;
import org.example.auth.model.param.PhoneRegisterSendCodeParam;
import org.example.auth.service.UserService;
import org.example.auth.utils.SecurityUserContext;
import org.example.message.constant.SMSVerifyCodeType;
import org.example.message.dto.SendOrVerifyOfCodeDTO;
import org.example.message.service.SMSMessageService;
import org.example.security.core.annotation.Anonymous;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAuthenticationController {
    private final UserService userService;

    private final SMSMessageService smsMessageService;

    @PostMapping("/info")
    public User info() {
        Integer userId = SecurityUserContext.getUserId();
        return userService.getById(userId);
    }

    @PostMapping("/phoneLogin")
    @Anonymous
    public LoginResponseDTO login(@RequestBody @Valid PhonePasswordLoginParam param) {
        String token = userService.phonePasswordLogin(param);
        return new LoginResponseDTO(token);
    }

    @PostMapping("/phoneCodeLogin")
    @Anonymous
    public LoginResponseDTO login(@RequestBody @Valid PhoneCodeLoginParam param) {
        String token = userService.phoneCodeLogin(param);
        return new LoginResponseDTO(token);
    }

    @PostMapping("/phoneRegister/sendCode")
    @Anonymous
    public void registerSendCode(@RequestBody @Valid PhoneRegisterSendCodeParam param) {
        smsMessageService.sendCode(
                new SendOrVerifyOfCodeDTO()
                        .setPhone(param.phone())
                        .setType(SMSVerifyCodeType.Register)
                        .setSource(param.type().name())
        );
    }

    @PostMapping("/phoneRegister")
    @Anonymous
    public void register(@RequestBody @Valid PhoneRegisterParam param) {
        userService.phoneRegister(param);
    }
}
