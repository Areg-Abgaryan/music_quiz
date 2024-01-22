/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secret;
    private final long validTimeMillis;
    private final UserDetailsService userDetailsService;

    public JwtProvider(UserDetailsService userDetailsService, @Value("${jwt.secret}") String secret,
                       @Value("${jwt.expired}") long validTimeMillis) {
        this.userDetailsService = userDetailsService;
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validTimeMillis = validTimeMillis;
    }

    public String createToken(String username) {
        final Claims claims = Jwts.claims().setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validTimeMillis))
                //  FIXME !! Do smth with this deprecated api
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return bearerToken != null && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }

    public boolean isTokenValid(String token) {
        try {
            if (StringUtils.isBlank(token) || ! token.startsWith("Bearer : ")) {
                throw new JwtException("Token format is invalid !");
            }
            final Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            //log.error("JWT token is expired or invalid");
            return false;
        }
    }
}
