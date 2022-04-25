package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.security.domain.DomainUser;
import com.example.security.domain.LoginUser;
import com.example.security.mapper.AuthorityMapper;
import com.example.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername");
        //查询用户信息
        LambdaQueryWrapper<DomainUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DomainUser::getId,username);
        DomainUser domainUser = userMapper.selectOne(queryWrapper);
        // 如果没有查询到用户，就抛出异常
        if (Objects.isNull(domainUser)){
            throw new RuntimeException("用户不存在");
        }
        System.out.println(domainUser);


        //TODO 查询权限信息
        List<String> authorities = authorityMapper.selectAuthoritiesByUserid(domainUser.getId());
//        authorities.add("test");
//        authorities.add("admin");



        // 把数据封装成userdetial返回
        return new LoginUser(domainUser,authorities);
    }
}
