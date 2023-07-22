package com.ferdev.cafe.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.token.secret}")
    private String secretKey;

    public String generateToken(String username, String role){
        Map<String, Object> claims= new HashMap<>();
        claims.put("subject", username);
        claims.put("role", role);

        long expirationTime = 2592000L * 1000L;
        Date dateNow= new Date(System.currentTimeMillis());
        Date dateExpiration=  new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(dateNow)
                .setExpiration(dateExpiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        final Claims claims= extractAllClaims(token);
        return claims.get("subject").toString();
    }

    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

}

