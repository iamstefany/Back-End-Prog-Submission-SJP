package stefany.piccaro.submission.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.dto.LoginRequestDTO;
import stefany.piccaro.submission.dto.LoginResponseDTO;
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

    public AuthInfoDTO getAuthInfo(HttpServletRequest httpRequest) {
        // Get token from request
        return jwtTools.getAuthInfoFromHTTPRequest(httpRequest);
    }

    public LoginResponseDTO attemptLogin(LoginRequestDTO request) {
        // Check if user exists
        User found = userService.findByEmail(request.email());

        // If user found -> check password
        if (bcrypt.matches(request.password(), found.getPassword())) {
            // If password matches > return token
            return new LoginResponseDTO(jwtTools.createToken(found), found.getUserId());
        }

        // If password is incorrect -> throw exception
        throw new UnauthorizedException("Incorrect credentials.");
    }
}
