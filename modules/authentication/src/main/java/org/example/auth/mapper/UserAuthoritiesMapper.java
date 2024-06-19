package org.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.auth.model.entity.UserAuthorities;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Mapper
public interface UserAuthoritiesMapper extends BaseMapper<UserAuthorities> {

}
