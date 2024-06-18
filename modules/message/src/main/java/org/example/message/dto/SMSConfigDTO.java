package org.example.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SMSConfigDTO {
    /**
     * 短信发送通道
     */
    @NotBlank(message = "发送通道不能为空")
    private String channel;

    private String appId;

    private String accessKey;

    private String secretKey;

    private String templateId;

    private String signature;

    private String account;

    private String password;
}
