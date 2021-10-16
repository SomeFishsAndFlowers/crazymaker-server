package com.jwl.fallback;

import com.jwl.client.UserClient;
import com.jwl.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        log.error("rpc 调用异常, { }", throwable);
        return new UserClient() {
            @Override
            public User getUserById(Long id) {
                return new User(0L , "fallbackFactory", "123");
            }

            @Override
            public String echo(String echo) {
                return "fallbackFactory";
            }
        };
    }
}
