package com.library.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey = "";

    public JwtService() {
        try {
            SecretKey sk = KeyGenerator.getInstance("HmacSHA256").generateKey();
            secretKey = Base64.getUrlEncoder().withoutPadding().encodeToString(sk.getEncoded());

            //Base64.getUrlEncoder().withoutPadding().encodeToString(data)
        }catch (Exception e){
            throw new RuntimeException("No such Algo");
        }
    }


    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyByte = Base64.getUrlDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    private Claims extractAllClams(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token , Function<Claims,T> claimsTFunction){
        final Claims claims = extractAllClams(token);
        return claimsTFunction.apply(claims);
    }

    private Date expiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return expiration(token).before(new Date());
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validate(UserDetails userDetails,String token){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
