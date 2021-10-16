package com.jwl.config;

import com.jwl.filter.DemoAuthFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author wenlo
 */
public class DemoAuthConfig<T extends DemoAuthConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private DemoAuthFilter authFilter = new DemoAuthFilter();

    @Override
    public void configure(B builder) throws Exception {
        authFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        DemoAuthFilter filter = postProcess(authFilter);
        builder.addFilterBefore(filter, LogoutFilter.class);
    }
}
