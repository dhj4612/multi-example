package org.example.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.message.constant.SMSVerifyCodeType;
import org.example.message.validate.SendCodeValidate;
import org.example.message.validate.VerifyCodeValidate;

import java.util.Map;

@Data
@Accessors(chain = true)
public class SendOrVerifyOfCodeDTO {
    @NotBlank(message = "手机号不能为空", groups = {SendCodeValidate.class, VerifyCodeValidate.class})
    private String phone;

    @NotBlank(message = "校验验证码不能为空", groups = {SendCodeValidate.class})
    private String code;

    @NotBlank(message = "发送来源不能为空", groups = {SendCodeValidate.class, VerifyCodeValidate.class})
    private String source;

    @NotNull(message = "发送类型不能为空", groups = {SendCodeValidate.class, VerifyCodeValidate.class})
    private SMSVerifyCodeType type;

    private SMSConfigDTO config;

    /**
     * 扩展参数
     */
    private Map<String, Object> extend;
}
