package org.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.auth.model.entity.User;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
