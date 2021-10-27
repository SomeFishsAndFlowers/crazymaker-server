package com.jwl.user.remote.client;

import com.jwl.user.remote.fallback.UserClientFallbackFactory;
import com.jwl.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wenlo
 */
@FeignClient(value = "uaa-provider",
        path = "uaa-provider/api/user",
//        fallback = UserClientFallback.class,
        fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/detail/v1/{id}")
    User getUserById(@PathVariable("id") Long id);

    @GetMapping("/echo/v1/{echo}")
    String echo(@PathVariable("echo") String echo);

}
