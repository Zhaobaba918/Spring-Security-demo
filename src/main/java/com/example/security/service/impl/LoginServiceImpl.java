package com.example.security.service.impl;

import com.example.security.domain.DomainUser;
import com.example.security.domain.LoginUser;
import com.example.security.domain.ResponseResult;
import com.example.security.service.LoginService;
import com.example.security.utils.JwtUtil;
import com.example.security.utils.RedisCache;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service // 注入容器
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(DomainUser user) {

        //AuthenticationManager authenticate进行认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);//会调用userdetailservice中的方法进行校验

        //如果认证没通过通过抛出异常给提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }

        //如果通过了，使用userid生成jwt
        final LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getDomainUser().getId();
        final String jwt = JwtUtil.createJWT(id);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("login:" + id, loginUser);
        return new ResponseResult(200, "登录成功", map);
    }

    @Override
    public ResponseResult logout() {
        //SecurityContextHolder中的用户id，SecurityContextHolder是线程级的，也就是一个请求一个SecurityContextHolder,不需要删除context中的东西
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        final String id = loginUser.getDomainUser().getId();

        //删除redis中的值
        redisCache.deleteObject("login:"+id);
        return new ResponseResult(200,"注销成功");
    }
}
