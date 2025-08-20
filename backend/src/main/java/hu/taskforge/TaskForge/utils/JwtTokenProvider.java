package hu.taskforge.TaskForge.utils;

import hu.taskforge.TaskForge.user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private final String secretKey = "b9db85c09824c5f5f0e728a4ac945e69a4d2f0ca4afa0cc578a4e838102adf52";
    private final long validityInMs = 3600000; // 1 óra

    public String createToken(String username, Set<Role> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles",roles.stream().map(Role::getName).toList());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + this.validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256,this.secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
