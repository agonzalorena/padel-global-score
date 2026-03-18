package com.padel.padel_global_score.security.jwt;

import com.padel.padel_global_score.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    private static final String SECRET_KEY = "j7ZookpUTYxclaULynjypGQVKMYXqOXMI+/1sQ2gOV1BF6VOHw6OzYj9RNZY4GcHAE3Igrah3MZ26oLrY/3y4Q==";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final SecretKey key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    public TokenProvider() {
        //transformo la clave secreta que es un string en bytes para que la pueda usar jjwt
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        //espera un una clave minima de 256 bits (32 bytes) para HS256
        this.key = Keys.hmacShaKeyFor(keyBytes);
        // creo el parser con la clave secreta para despues validar los tokens
        this.jwtParser = Jwts.parser().verifyWith(key).build();
        this.tokenValidityInMilliseconds = Duration.ofDays(1).toMillis(); // nuevo metodo, valido por 1 dia.
    }

    public String createToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .claim("userId", userDetails.getUserId())
                .claim("username", userDetails.getUsername())
                .signWith(key)
                .expiration(validity)
                .issuedAt(new Date())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        String username = claims.get("username", String.class);
        Long userId = claims.get("userId", Long.class);
        if (username == null) throw new RuntimeException("Token invalido");
        Collection<? extends GrantedAuthority> authorities = List.of();
        CustomUserDetails principal = new CustomUserDetails(userId, username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            final var claims = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(authToken);
            this.checkTokenExpiration(claims);
            return true;
        } catch (UnsupportedJwtException e) {
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }

    private void checkTokenExpiration(Jws<Claims> token) {
        try {
            final var payload = token.getPayload();
            if (payload.getExpiration().before(new Date()) || payload.getIssuedAt().after(new Date((new Date()).getTime() + this.tokenValidityInMilliseconds)))
                throw new ExpiredJwtException(null, null, null);
        } catch (Exception e) {
            throw new ExpiredJwtException(null, null, null);
        }
    }
}
