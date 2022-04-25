package com.example.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //convert  to json
public class HelloController {

    @RequestMapping("/hello")
    @PreAuthorize("hasAnyAuthority('A1')")
    public String hello(){
        return "hello";
    }

}
