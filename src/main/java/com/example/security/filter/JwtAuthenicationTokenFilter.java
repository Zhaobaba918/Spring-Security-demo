package com.example.security.filter;

import com.example.security.domain.LoginUser;
import com.example.security.utils.JwtUtil;
import com.example.security.utils.RedisCache;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component  //注入spring容器中
public class JwtAuthenicationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token,请求头中获取
        final String token = request.getHeader("token");
        if (StringUtils.isNullOrEmpty(token)) {
            //放行
            filterChain.doFilter(request, response);

            //dofilter执行完了以后会返回（filter是层层调用的而不是顺序的）
            return;
        }

        //解析token
        String userid;
        try {
            final Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder,供后面的filter看有没有授权
        //用三参构造函数是因为三参构造函数会把标志位设置为true，就是已经授权
        //TODO 获取权限信息封装到Authentication
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities()));
        //放行
        filterChain.doFilter(request, response);
    }
}
