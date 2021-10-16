package com.jwl.fallback;

import com.jwl.client.UserClient;
import com.jwl.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public User getUserById(Long id) {
        return new User(0L, "userClientFallback", "456");
    }

    @Override
    public String echo(String echo) {
        return "userClientFallback";
    }
}
