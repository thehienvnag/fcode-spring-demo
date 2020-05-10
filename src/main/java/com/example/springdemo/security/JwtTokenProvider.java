package com.example.springdemo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret = "827c66f543f0b91090cda7b9eac2b6bd80c4728e3f9d50aa468d9d091a12249";
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
    Key key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs = 120000;

    public String generateToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + jwtExpirationInMs);


        String token = Jwts.builder()
                    .setSubject(userPrincipal.getId().toString())
                    .setIssuedAt(now)
                    .setExpiration(expiredDate)
                    .signWith(key)
                    .compact();

        return token;
    }

    public Integer getUserIdFromJWT(String token){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            logger.error("ExpiredJwt: " + e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("UnsupportedJwt: " + e.getMessage());
        } catch (MalformedJwtException e){
            logger.error("MalformedJwt: " + e.getMessage());
        } catch (IllegalArgumentException e){
            logger.error("IllegalArgument: " + e.getMessage());
        }
        return false;
    }

}
