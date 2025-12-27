package stefany.piccaro.submission.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.dto.LoginRequestDTO;
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.UnauthorizedException;
import stefany.piccaro.submission.security.JWTTools;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public AuthInfoDTO getAuthInfo(HttpServletRequest request) {
        // Get token from request
        String token = jwtTools.getCurrentToken(request);

        // Validate token
        jwtTools.verifyToken(token);

        // Extract user ID from token
        UUID userId = jwtTools.getUserIDFromToken(token);

        // Load user from DB
        User user = userService.findById(userId);

        return new AuthInfoDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getIsBlocked(),
                user.getRoleNames(),
                token
        );
    }

    public String attemptLogin(LoginRequestDTO request) {
        // Check if user exists
        User found = userService.findByEmail(request.email());

        // If user found > check password
        if (bcrypt.matches(request.password(), found.getPassword())) {
            // If password matches > return token
            return jwtTools.createToken(found);
        }

        // If password is incorrect > throw exception
        throw new UnauthorizedException("Incorrect credentials.");
    }
}
