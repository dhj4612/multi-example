package org.example.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class SendConfigDTO {
    /**
     * 短信发送通道
     */
    @NotBlank(message = "发送通道不能为空")
    private String channel;

    /**
     * 发送参数
     */
    private Map<String, Objects> params;
}
