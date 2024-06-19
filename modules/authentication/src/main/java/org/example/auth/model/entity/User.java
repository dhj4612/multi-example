package org.example.auth.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.example.auth.constant.UserState;
import org.example.auth.constant.UserType;
import org.example.core.base.BaseEntity;

import java.io.Serial;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Getter
@Setter
@TableName("user")
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("version")
    @Version
    private Integer version;

    /**
     * C-C端用户 S-系统用户
     */
    @TableField("user_type")
    private UserType userType;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * Normal-正常 Locking-锁定
     */
    @TableField("state")
    private UserState state;
}
