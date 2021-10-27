package com.jwl.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author wenlo
 */
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class TestFilter implements Filter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TestFilter() {
        System.out.println("TestFilter constructor....");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("do filter, passwordEncoder: {}", passwordEncoder);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
