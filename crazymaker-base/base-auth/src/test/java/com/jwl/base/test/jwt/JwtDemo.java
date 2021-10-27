package com.jwl.base.test.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
public class JwtDemo {
    /**
     * JWT usually contains header.payload.digest
     *
     * header: {
     *     "typ": "JWT",
     *     "alg": "HS256"
     * }
     *
     * payload: {
     *     "sub": "session id",
     *     "exp": ..., // expire time
     *     "iat": ..., // issuance time
     *     ... // custom field
     * }
     *
     * digest: SHA(header.payload)
     */

    @Test
    public void testBaseJWT() {
        try {
            String subject = "session id";
            String salt = "user password";
            Algorithm algorithm = Algorithm.HMAC256(salt);
            long start = System.currentTimeMillis() - 60000;
            Date end = new Date(start + 30*60*1000);
            String token = JWT.create()
                    .withClaim("custom", "test")
                    .withSubject(subject)
                    .withIssuedAt(new Date(start))
                    .withExpiresAt(end)
                    .sign(algorithm);
            log.info(token);
            log.info(StringUtils.newStringUtf8(Base64.decodeBase64(token.split("\\.")[0])));
            log.info(StringUtils.newStringUtf8(Base64.decodeBase64(token.split("\\.")[1])));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
