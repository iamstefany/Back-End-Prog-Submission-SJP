package stefany.piccaro.submission.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.UnauthorizedException;

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
                // Set subject as user ID
                .subject(String.valueOf(user.getUserId()))
                // Include additional claims -> no need to query DB again for these data
                .claim("firstName", user.getRoles())
                .claim("lastName", user.getRoles())
                .claim("email", user.getRoles())
                .claim("isBlocked", user.getRoles())
                .claim("roles", user.getRoles())
                .claim("isSuperAdmin", user.getAdminProfile() != null && user.getAdminProfile().getIsSuperAdmin())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // Returns AuthInfoDTO extracted from the JWT token present in the request's Authorization header
    public AuthInfoDTO getAuthInfoFromRequest(HttpServletRequest request) {
        String token = getCurrentToken(request);
        Claims claims = getClaims(token); // Token is verified inside getClaims method

        UUID userId = UUID.fromString(claims.getSubject());
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);
        String email = claims.get("email", String.class);
        Boolean isBlocked = claims.get("isBlocked", Boolean.class);
        int roles = claims.get("roles", Integer.class);
        boolean isSuperAdmin = claims.get("isSuperAdmin", Boolean.class);

        return new AuthInfoDTO(userId, firstName, lastName, email, isBlocked, roles, isSuperAdmin, token);
    }

    // Gets current token from Authorization header in the request
    private String getCurrentToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed Authorization header");
        }

        return authorizationHeader.replace("Bearer ", "");
    }

    // Verifies token and returns its claims, or throws UnauthorizedException if token is invalid
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}