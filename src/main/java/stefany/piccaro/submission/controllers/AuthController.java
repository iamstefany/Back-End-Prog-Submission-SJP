package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.dto.*;
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.exceptions.UnauthorizedException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.AuthService;
import stefany.piccaro.submission.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public AuthInfoDTO info(HttpServletRequest request) {
        return authService.getAuthInfo(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO body) {
        return new LoginResponseDTO(authService.attemptLogin(body));
    }

    @PostMapping("/register/{role}")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponseDTO createUser(
            @PathVariable("role") String role,
            @RequestBody @Validated SignUpRequestDTO request,
            BindingResult validationResult
    ) {
        // Try to convert role string to enum
        Role userRole;
        try { userRole = Role.valueOf(role.toUpperCase()); }
        catch (IllegalArgumentException ex) { throw new ValidationException("Invalid role: " + role); }

        // Prevent ADMIN role registration - can only be done within /admin endpoints
        if (userRole == Role.ADMIN) {
            throw new ForbiddenException("Cannot register users with ADMIN role from this endpoint.");
        }

        // Basic DTO validation
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
        }

        // Role-specific validation
        if (userRole == Role.GUEST) {
            if (request.phoneNumber() == null || request.phoneNumber().isBlank()) {
                throw new ValidationException("Phone number is mandatory for guests.");
            }
            if (request.dateOfBirth() == null) {
                throw new ValidationException("Date of birth is mandatory for guests.");
            }
        }

        // Save user with assigned role
        return userService.save(request, userRole);
    }
}