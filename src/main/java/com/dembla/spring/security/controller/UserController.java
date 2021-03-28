package com.dembla.spring.security.controller;


import com.dembla.spring.security.response.UserRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    Environment env ;

    @GetMapping("/status/check")
    public String status(){
        return "working on port : " + env.getProperty("local.server.port") ;
    }

//    @Secured("ROLE_developer")
    @PreAuthorize("hasAuthority('ROLE_developer')  or #id == #jwt.subject") // security Expression
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id , @AuthenticationPrincipal Jwt jwt){
        return "Deleted user with userid = " + id  + "and JWT subject " + jwt.getSubject();
    }

    @PostAuthorize("returnObject.userId == #jwt.subject")
    @GetMapping(path ="/{id}")
    public UserRest getUser(@PathVariable String id , @AuthenticationPrincipal Jwt jwt ){
           return new UserRest("Mayank","Dembla",id) ;
    }


}
