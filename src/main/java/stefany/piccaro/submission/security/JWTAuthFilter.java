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
import stefany.piccaro.submission.dto.AuthInfoDTO;
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
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);
            List<GrantedAuthority> authorities = new ArrayList<>();

            // Issue authorities based on roles if user is not blocked
            if (!authInfo.isBlocked()) {
                authorities = Arrays.stream(Role.values())
                        .filter(r -> Role.hasRole(authInfo.roles(), r))
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                        .collect(Collectors.toList());
            }
            else {
                throw new UnauthorizedException("Your account is blocked.");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(authInfo.userId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continue filter chain
            filterChain.doFilter(httpRequest, response);
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
    protected boolean shouldNotFilter(HttpServletRequest httpRequest) throws ServletException {
        // Do not filter requests at /auth/
        return new AntPathMatcher().match("/auth/**", httpRequest.getServletPath());
    }
}
