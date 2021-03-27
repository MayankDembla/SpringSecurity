package com.dembla.spring.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
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

    @Secured("ROLE_developer")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id){
        return "Deleted user with userid = " + id ;
    }

}
