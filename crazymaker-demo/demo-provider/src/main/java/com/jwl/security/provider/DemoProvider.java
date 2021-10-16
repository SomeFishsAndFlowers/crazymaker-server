package com.jwl.security.provider;

import com.jwl.security.token.DemoToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wenlo
 */
public class DemoProvider implements AuthenticationProvider {

    private final static Map<String, String> users = new LinkedHashMap<>();
    static {
        users.put("zhangsan", "123456");
        users.put("jiwenlong", "961025");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DemoToken demoToken = (DemoToken) authentication;
        String pwd = users.get(demoToken.getUsername());
        if(!StringUtils.isEmpty(pwd) && pwd.equals(demoToken.getPassword())) {
            demoToken.setAuthenticated(true);
            return demoToken;
        }
        throw new BadCredentialsException("用户名或密码错误");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(DemoToken.class);
    }
}
