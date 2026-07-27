package com.leandro.shop.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Getter
    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> extraClaims,UUID uuid){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(uuid.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey())
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public UUID extractSubject(String token){
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public String extractUserRole(String token){
        return extractClaim(token, claims -> claims.get("user_role", String.class));
    }

    public String extractAccountStatus(String token){
        return extractClaim(token, claims -> claims.get("account_status", String.class));
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UUID userUUID){
        final UUID extractedUUID = extractSubject(token);
        return (extractedUUID.equals(userUUID) && !isTokenExpired(token));
    }
}
