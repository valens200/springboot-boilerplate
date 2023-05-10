package com.ebcr.utils;

import com.ebcr.models.User;
import com.ebcr.security.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.flogger.Flogger;
import org.apache.logging.slf4j.SLF4JLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenGenerator {
    @Value("${jwt.expiresIn}")
    private int expiryDate;
    @Value("${jwt.secret}")
    private String privateKey;
    private User user = null;
    private Date now  = new Date();

        public String generateAccessToken(org.springframework.security.core.Authentication authentication){
            com.ebcr.security.UserPrincipal userPrincipal =(UserPrincipal) authentication.getPrincipal();
            Date expirationDate = new Date(now.getTime() + expiryDate);
            Set<GrantedAuthority>grantedAuthorities = new HashSet<>();
            for(GrantedAuthority role : userPrincipal.getAuthorities()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
            }

            return  Jwts.builder()
                    .setId(userPrincipal.getUsername() + "k")
                    .setSubject(userPrincipal.getUsername())
                    .claim("user", userPrincipal)
                    .claim("authoritiens", grantedAuthorities)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS256, privateKey)
                    .compact();
        }
        public String generateRefreshToken(Authentication authentication) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Date expirationDate = new Date(now.getTime() + expiryDate);

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (GrantedAuthority role : userPrincipal.getAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
            }
            return Jwts.builder()
                    .setId(userPrincipal.getUsername() + "k")
                    .setSubject(userPrincipal.getUsername())
                    .claim("user", userPrincipal)
                    .claim("authorities", userPrincipal.getAuthorities())
                    .signWith(SignatureAlgorithm.HS256, privateKey)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .compact();
        }

        public String getUserIdFromToken(String token){
           Claims claims =Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
           return claims.getSubject();
        }
        public  boolean validateToken(String token ){
            try{
                Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
                return true;
            }catch (SignatureException e){
                System.out.println("Invalid jwt signature  " + e.getMessage());
            }catch (MalformedJwtException e){
                System.out.println("Invalid jwt token " + e.getMessage());
            }catch (ExpiredJwtException e){
                System.out.println("Jwt expired " + e.getMessage());
            }catch (UnsupportedJwtException e){
                System.out.println("Unsupported jwt token " + e.getMessage());
            }catch (IllegalArgumentException e){
                System.out.println("Jwt claims string is empty");
            }
            return false;
        }


}
