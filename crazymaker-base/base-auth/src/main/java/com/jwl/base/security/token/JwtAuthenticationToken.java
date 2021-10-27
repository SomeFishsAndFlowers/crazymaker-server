package com.jwl.base.security.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author jiwenlong
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {


    private UserDetails userDetails;

    private DecodedJWT decodedJWT;

    public JwtAuthenticationToken(UserDetails userDetails, DecodedJWT jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDetails = userDetails;
        this.decodedJWT = jwt;
    }

    public JwtAuthenticationToken(DecodedJWT decodedJWT) {
        super(null);
        this.decodedJWT = decodedJWT;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public UserDetails getDetails() {
        return userDetails;
    }

    public DecodedJWT getDecodedJWT() {
        return decodedJWT;
    }
}
