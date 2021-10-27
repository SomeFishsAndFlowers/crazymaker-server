package com.jwl.demo.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DemoAuthFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler failureHandler = new AuthenticationFailureHandler() {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(exception.getMessage());
            response.flushBuffer();
            log.info(exception.getMessage());
        }
    };

    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuthenticationException failed = null;
        log.info("web security");
        try {
            Authentication returnToken;
            boolean succeed;
            String username = request.getParameter("username");
            String password = request.getParameter("password");
//            DemoToken token = new DemoToken(username, password);
            UserDetails userDetails = User.builder().username(username).password(password).authorities("USER").build();
            Authentication token = new UsernamePasswordAuthenticationToken(userDetails, password);
            returnToken = getAuthenticationManager().authenticate(token);
            succeed = returnToken.isAuthenticated();
            if(succeed) {
                SecurityContextHolder.getContext().setAuthentication(returnToken);
                filterChain.doFilter(request, response);
                return;
            }
        } catch (AuthenticationException e) {
            failed = e;
        }
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }


    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
