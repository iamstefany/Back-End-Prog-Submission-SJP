package stefany.piccaro.submission.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import stefany.piccaro.submission.entities.User;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {

    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    // Returns JWT token for given user
    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Expires in 7 days
                .subject(String.valueOf(user.getUserId()))
                .claim("roles", user.getRoles()) // Include user roles in the token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // Verifies token, and throws UnauthorizedException if token is invalid
    public void verifyToken(String accessToken) {
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(accessToken);
    }

    // Get User ID from token
    public UUID getUserIDFromToken(String accessToken) {
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject());
    }

    // Get User Roles from token
    public int getRolesFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("roles", Integer.class);
    }
}