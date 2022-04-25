package com.example.security.service;

import com.example.security.domain.DomainUser;
import com.example.security.domain.ResponseResult;

public interface LoginService {
    ResponseResult login(DomainUser user);

    ResponseResult logout();
}
