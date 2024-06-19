package org.example.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.auth.mapper.UserAuthoritiesMapper;
import org.example.auth.model.entity.UserAuthorities;
import org.example.auth.service.UserAuthoritiesService;
import org.springframework.stereotype.Service;

/**
 * @author dhj
 * @since 2024-05-11
 */
@Service
public class UserAuthoritiesServiceImpl extends ServiceImpl<UserAuthoritiesMapper, UserAuthorities>
        implements UserAuthoritiesService {

}
