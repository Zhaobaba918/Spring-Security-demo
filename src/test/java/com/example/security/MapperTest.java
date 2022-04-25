package com.example.security;

import com.example.security.domain.DomainUser;
import com.example.security.mapper.AuthorityMapper;
import com.example.security.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Test
    public void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("1951459"));
        System.out.println(passwordEncoder.encode("1951748"));
//        System.out.println(passwordEncoder.matches("1234","$2a$10$9c9xxYfXqhFziA6qjyV7y.i.fo3zft240u.gMj6q3OoPILyr2Y3kO"));
//        System.out.println(passwordEncoder.matches("1234","$2a$10$aW9Ot/7IPHHn4YPobVVcDu.J.0.WmYMl9rfrakN2VW0fXXmgjRsNW"));
//        System.out.println(passwordEncoder.matches("12304","$2a$10$aW9Ot/7IPHHn4YPobVVcDu.J.0.WmYMl9rfrakN2VW0fXXmgjRsNW"));

    }


    @Test
    public void testUserMapper(){
        List<DomainUser> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void test(){
        List<String> authorities = authorityMapper.selectAuthoritiesByUserid("1951459");
        System.out.println(authorities);
    }

}
