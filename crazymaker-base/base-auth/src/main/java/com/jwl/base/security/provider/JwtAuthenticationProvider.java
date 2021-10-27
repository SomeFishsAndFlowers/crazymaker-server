package com.jwl.base.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jwl.base.security.token.JwtAuthenticationToken;
import com.jwl.common.constant.SessionConstants;
import com.jwl.common.context.SessionHolder;
import com.jwl.common.dto.UserDTO;
import com.jwl.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import java.util.Calendar;

/**
 * @author jiwenlong
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private RedisOperationsSessionRepository sessionRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        DecodedJWT jwt = jwtToken.getDecodedJWT();
        if(jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
            throw new NonceExpiredException("认证过期");
        }
        // session id
        String sid = jwt.getSubject();
        // 令牌
        String newToken = jwt.getToken();
        Session session = null;
        try {
            session = sessionRepository.findById(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == session) {
            throw new NonceExpiredException("还没有登陆，请登陆");
        }
        String json = session.getAttribute(SessionHolder.G_USER);
        if(StringUtils.isBlank(json)) {
            throw new NonceExpiredException("认证有误，请重新认证");
        }
        UserDTO userDTO = JsonUtil.jsonToPojo(json, UserDTO.class);
        if (null == userDTO) {
            throw new NonceExpiredException("认证有误，请重新认证");
        }
        if (null == newToken || !newToken.equals(userDTO.getToken())) {
            throw new NonceExpiredException("您已在其他地方登陆");
        }
        String userID = null;
        if(null == userDTO.getUserId()) {
            userID = String.valueOf(userDTO.getId());
        }
        else {
            userID = String.valueOf(userDTO.getUserId());
        }
        UserDetails userDetails = User.builder()
                .username(userID)
                .password(userDTO.getPassword())
                .authorities(SessionConstants.USER_INFO)
                .build();
        try {
            String encryptSalt = userDTO.getPassword();
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(sid)
                    .build();
            verifier.verify(newToken);
        } catch (Exception e) {
            throw new NonceExpiredException("认证有误：令牌校验失败，请重新登陆", e);
        }
        JwtAuthenticationToken passedToken = new JwtAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
        passedToken.setAuthenticated(true);
        return passedToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(JwtAuthenticationToken.class);
    }

    public RedisOperationsSessionRepository getSessionRepository() {
        return sessionRepository;
    }

    public void setSessionRepository(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
}
