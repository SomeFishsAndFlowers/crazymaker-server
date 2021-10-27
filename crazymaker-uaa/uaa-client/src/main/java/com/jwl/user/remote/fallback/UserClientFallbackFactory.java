package com.jwl.user.remote.fallback;

import com.jwl.user.remote.client.UserClient;
import com.jwl.user.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jiwenlong
 */
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
