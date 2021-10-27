package com.jwl.base.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.jwl.base.security.token.JwtAuthenticationToken;
import com.jwl.common.constant.SessionConstants;
import com.jwl.common.context.SessionHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiwenlong
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication passedToken = null;
        AuthenticationException failed = null;

        String token = null;

        String sessionIDStore = SessionHolder.getSessionIDStore();
        if(sessionIDStore.equals(SessionConstants.SESSION_STORE)) {
            token = request.getHeader(SessionConstants.SESSION_STORE);
        }
        else if (sessionIDStore.equals(SessionConstants.ADMIN_SESSION_STORE)) {
            token = request.getHeader(SessionConstants.ADMIN_AUTHORIZATION_HEAD);
        }
        else {
            failed = new InsufficientAuthenticationException("请求头认证消息为空");
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        token = StringUtils.removeStart(token, "Bearer ");
        try {
            if(StringUtils.isNoneBlank(token)) {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(JWT.decode(token));
                passedToken = getAuthenticationManager().authenticate(authToken);
                UserDetails details = (UserDetails) passedToken.getDetails();

                request.setAttribute(SessionConstants.USER_IDENTIFIER, details.getUsername());
            }
            else {
                failed = new InsufficientAuthenticationException("请求头认证消息为空");
            }
        } catch (JWTDecodeException | AuthenticationException e) {
            failed = new InsufficientAuthenticationException("认证失败", e);
            unsuccessfulAuthentication(request, response, failed);
        }
        filterChain.doFilter(request, response);
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    }
}
