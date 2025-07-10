package com.ticket_management_system.auth_service.auth_service.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class JwtUtils {
    @Value( "${jwt.secret}")
    private String secretKey;

    @Value( "${jwt.expiration}")
    private int expiry;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);    public String getJwtTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(authToken);
            return true;
        }  catch (MalformedJwtException e) {
        logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        logger.error("JWT claims string is empty: {}", e.getMessage());
    }
        return false;
    }

    private Key Key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateJwtToken(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(java.util.Date.from(java.time.Instant.now()))
                .setExpiration(java.util.Date.from(java.time.Instant.now().plusSeconds(expiry)))
                .signWith(Key())
                .compact();
    }

    public String getUserNameFromJwtToken(String authToken){
        return Jwts.parser()
                .verifyWith((SecretKey) Key())
                .build()
                .parseSignedClaims(authToken)
                .getPayload().getSubject();
    }


}
