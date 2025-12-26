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
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");

        // Validate token
        jwtTools.verifyToken(token);

        // Extract user ID from token
        UUID userId = jwtTools.getUserIDFromToken(token);

        // Load user from DB
        User user = userService.findById(userId);

        // Convert roles bitmask to list of names
        List<String> roleNames = new ArrayList<>();
        for (Role role : Role.values()) {
            if ((user.getRoles() & role.getBit()) != 0) {
                roleNames.add(role.name());
            }
        }

        return new AuthInfoDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getIsBlocked(),
                roleNames,
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
