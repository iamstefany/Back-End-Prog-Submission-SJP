package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stefany.piccaro.submission.dto.LoginRequestDTO;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.UnauthorizedException;
import stefany.piccaro.submission.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String attemptLogin(LoginRequestDTO request) {
        // Check if user exists
        User found = this.userService.findByEmail(request.email());

        // If user found > check password
        if (bcrypt.matches(request.password(), found.getPassword())) {
            // If password matches > return token
            return jwtTools.createToken(found);
        }

        // If user not found or password is incorrect > throw exception
        throw new UnauthorizedException("Incorrect credentials.");
    }
}
