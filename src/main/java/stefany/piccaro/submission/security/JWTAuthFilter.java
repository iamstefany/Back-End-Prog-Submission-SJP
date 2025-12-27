package stefany.piccaro.submission.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.UnauthorizedException;
import stefany.piccaro.submission.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Get token from Authorization Header and verify
            String accessToken = jwtTools.getCurrentToken(request);
            jwtTools.verifyToken(accessToken);

            // Get user ID from token, find user, and set authentication in Security Context
            UUID userId = jwtTools.getUserIDFromToken(accessToken);
            User found = userService.findById(userId);

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (found != null) {
                // Grant authorities only if user is not blocked
                boolean isBlocked = found.getIsBlocked();

                if (!isBlocked) {
                    // Extract roles from token and set authorities
                    int rolesBitmask = jwtTools.getRolesFromToken(accessToken);
                    authorities = Arrays.stream(Role.values())
                            .filter(r -> Role.hasRole(rolesBitmask, r)) // use your helper
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                            .collect(Collectors.toList());
                }
                else {
                    throw new UnauthorizedException("Your account is blocked.");
                }
            } else {
                throw new UnauthorizedException("User not found.");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continue filter chain
            filterChain.doFilter(request, response);
        }
        // Handle UnauthorizedException
        catch (UnauthorizedException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
        // Handle other exceptions
        catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Malformed token.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Do not filter requests at /auth/
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
