package org.example.auth.model.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.auth.constant.UserType;

public record PhoneCodeLoginParam(
        @NotBlank(message = "手机号不能为空") String phone,
        @NotBlank(message = "验证码不能为空") String code,
        @NotNull(message = "用户类型不能为空") UserType type) {
}
