package com.employee.utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Secure 256-bit secret keys generated once per app startup
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);         // Access token key
    private static final SecretKey REFRESH_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Refresh token key

    public String extractUsername(String token) {
        return extractAllClaims(token, SECRET_KEY).getSubject();
    }

    public String extractUsernameFromRefresh(String token) {
        return extractAllClaims(token, REFRESH_SECRET_KEY).getSubject();
    }

    private Claims extractAllClaims(String token, SecretKey secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), 10 * 60 * 1000, SECRET_KEY); // 10 minutes
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), 24 * 60 * 60 * 1000, REFRESH_SECRET_KEY); // 1 day
    }

    private String createToken(String subject, long duration, SecretKey secretKey) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
