package com.jwl.user.remote.fallback;

import com.jwl.user.remote.client.UserClient;
import com.jwl.user.pojo.User;
import org.springframework.stereotype.Component;

/**
 * @author jiwenlong
 */
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
