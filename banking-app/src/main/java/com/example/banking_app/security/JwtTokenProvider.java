package com.example.banking_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component // why component "Spring, create an object of this class for me."
public class JwtTokenProvider {
    //Secret key used to sign JWT
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpiration;

    /*
     Creates SecretKey from the secret string.
     This key is used to sign and validate JWTs.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(
            String email,
            List<String> authorities) {

        return Jwts.builder()

                // User identity
                .setSubject(email)

                // Roles inside JWT
                .claim("roles", authorities)

                // Token creation time
                .setIssuedAt(new Date())

                // Expiry time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))

                // Digital signature
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    /*
     Extract email from JWT
     */
    public String getEmail(String token) {

        Claims claims = Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();

        return claims.getSubject();
    }

    /*
     Validate token
     */
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()

                    .setSigningKey(getSigningKey())

                    .build()

                    .parseClaimsJws(token);

            return true;

        }

        catch (ExpiredJwtException ex) {
            System.out.println("JWT expired");
        }

        catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT");
        }

        catch (SignatureException ex) {
            System.out.println("Invalid signature");
        }

        catch (IllegalArgumentException ex) {
            System.out.println("JWT is empty");
        }

        return false;
    }

    public String extractToken(String bearerToken){

        if(bearerToken != null &&
                bearerToken.startsWith("Bearer ")){

            return bearerToken.substring(7);
        }

        return null;
    }
}






