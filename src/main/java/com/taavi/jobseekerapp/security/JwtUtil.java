package com.taavi.jobseekerapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private Key key;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        logger.info("Initializing JWT Utility...");

        if (secretKey.length() < 32) {
            logger.error("JWT secret key is too short: {} characters", secretKey.length());
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long!");
        }

        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        logger.info("JWT key successfully initialized.");
    }

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.info("Generated JWT for user: {}", username);
        return token;
    }

    public String extractEmail(String token) {
        try {
            String email = parseClaims(token).getSubject();
            logger.info("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            logger.info("JWT is valid.");
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
