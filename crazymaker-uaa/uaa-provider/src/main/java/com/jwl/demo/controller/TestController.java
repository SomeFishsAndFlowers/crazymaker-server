package com.jwl.demo.controller;

import com.jwl.demo.user.proporty.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class TestController {

    @Autowired
    ConfigProperties configProperties;



    @GetMapping("/redis")
    public ConfigProperties configProperties() {
        System.out.println(configProperties);
        return configProperties;
    }

}
