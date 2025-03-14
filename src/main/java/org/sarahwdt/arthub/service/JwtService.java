package org.sarahwdt.arthub.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.model.user.Privilege;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String AUTHORITIES = "auth";
    private static final String ID = "id";
    private static final JacksonDeserializer<Map<String, ?>> deserializer = new JacksonDeserializer<>(
            Map.of(AUTHORITIES, PrivilegeSet.class));
    private static final JacksonSerializer<Map<String, ?>> serializer = new JacksonSerializer<>();

    private final Jwt properties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public CharSequence generateToken(JwtPrincipal principal) {
        return Jwts.builder()
                .json(serializer)
                .subject(principal.email())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.getExpiration().toMillis()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .claim(ID, principal.id())
                .claim(AUTHORITIES, new HashSet<>(principal.authorities()))
                .compact();
    }

    public JwtPrincipal extractValidPrincipal(CharSequence token) throws JwtException {
        Claims claims = Jwts.parser().verifyWith(getSigningKey()).json(deserializer).build()
                .parseSignedClaims(CharBuffer.wrap(token)).getPayload();
        String email = claims.getSubject();
        Integer id = claims.get(ID, Integer.class);
        Set<Privilege> authorities = claims.get(AUTHORITIES, PrivilegeSet.class);
        Date expiration = claims.getExpiration();
        if (email == null || id == null || authorities == null || expiration == null) {
            throw new JwtException("Invalid token");
        }
        return new JwtPrincipal(id, email, authorities);
    }

    private static class PrivilegeSet extends HashSet<Privilege> {
    }
}
