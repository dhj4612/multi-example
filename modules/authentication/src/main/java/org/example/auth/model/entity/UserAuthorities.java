package org.example.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.example.auth.constant.UserAuthoritiesState;
import org.example.auth.constant.UserAuthoritiesType;
import org.example.auth.constant.UserType;
import org.example.core.base.BaseEntity;
import org.example.mysql.core.DbFieldCryptoHandler;

import java.io.Serial;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Getter
@Setter
@TableName(value = "user_authorities", autoResultMap = true)
public class UserAuthorities extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("version")
    @Version
    private Integer version;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 授权类型 Phone-手机 Email-邮箱 WeChat-微信
     */
    @TableField("identity_type")
    private UserAuthoritiesType identityType;

    /**
     * 授权唯一标识，手机号，邮箱，openId等
     */
    @TableField(value = "identifier", typeHandler = DbFieldCryptoHandler.class)
    private String identifier;

    /**
     * 授权凭证，密码或第三方token等
     */
    @TableField("credential")
    private String credential;

    /**
     * Enable-启用 Disable-禁用
     */
    @TableField("state")
    private UserAuthoritiesState state;

    /**
     * C-C端用户 S-系统用户
     */
    @TableField("user_type")
    private UserType userType;
}
