package com.jwl.config;

import com.jwl.security.provider.DemoProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author wenlo
 */
@EnableWebSecurity
public class DemoWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .antMatchers("/demo/security")
                .authenticated()
                .and()
                .apply(new DemoAuthConfig<>())
                .and()
                .sessionManagement().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(demoProvider());
    }

    @Bean("demoAuthProvider")
    protected DemoProvider demoProvider() {
        return new DemoProvider();
    }
}
