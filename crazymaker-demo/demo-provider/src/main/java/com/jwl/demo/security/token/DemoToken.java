package com.jwl.demo.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;
import java.util.Collections;

/**
 * @author wenlo
 */
public class DemoToken extends AbstractAuthenticationToken {

    private String username;
    private String password;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public DemoToken(String username, String password) {
        super(Collections.emptyList());
        this.username = username;
        this.password = password;
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
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
