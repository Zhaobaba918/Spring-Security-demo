package com.example.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.security.domain.DomainUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthorityMapper extends BaseMapper<DomainUser> {

    @Select("SELECT AUTHORITY_NAME FROM T_USER NATURAL JOIN T_ROLE_AUTHORITY WHERE ID = #{userid}")
    List<String> selectAuthoritiesByUserid(String userid);
}
