package com.jwl.demo.controller;

import com.jwl.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenlo
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/detail/v1/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("{} visit", id);
        return new User(id, "纪文龙", "123456");
    }

    @GetMapping("/echo/v1/{echo}")
    public String echo(@PathVariable("echo") String echo) {
        log.info("{}", echo);
        return echo;
    }

}
