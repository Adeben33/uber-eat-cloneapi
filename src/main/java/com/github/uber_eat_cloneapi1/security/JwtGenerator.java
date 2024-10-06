package com.github.uber_eat_cloneapi1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import javax.crypto.SecretKey;
import java.security.SignatureException;
import java.util.stream.Collectors;
import java.util.Date;

@Component
public class JwtGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwtGenerator.class);


    private final SecretKey jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date currentDate = new Date();

        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION );


        String token = Jwts.builder()
                .setSubject(email)
                .claim("roles", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(jwtSecretKey)
                .compact();

        log.debug("Processing JWT token: {}", token);
        log.debug("Setting Authentication in SecurityContext for user: {}", email);

        return token;
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey )
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

//    public boolean validateToken(String token) {
//        log.debug("Validating JWT token: {}", token);
//        try {
//            Jwts.parser().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            throw new AuthenticationCredentialsNotFoundException("JWT was expied or incorrect");
//        }
//    }

    public boolean validateToken(String token) {
        log.debug("Validating JWT token: {}", token);
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT signature is invalid");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT token has expired");
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public boolean inValidateToken(String token){
        log.debug("InValidating JWT token: {}", token);

        try {
            Jwts.parser().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT signature is invalid");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT token has expired");
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

}
