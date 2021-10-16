package com.jwl.controller;

import com.jwl.client.UserClient;
import com.jwl.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wenlo
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Qualifier("com.jwl.client.UserClient")
    @Autowired
    UserClient userClient;

    @GetMapping("/user")
    public User get() {
        return userClient.getUserById(2L);
    }

    @GetMapping("/echo/{echo}")
    public String echo(@PathVariable("echo") String echo) {
        return userClient.echo(echo);
    }

    @GetMapping("/hystrix")
    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
    public String hystrix() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return "success";
    }

    private String fallback() {
        return "failure";
    }

    @GetMapping("/security")
    public String security() {
        return "success";
    }
}
