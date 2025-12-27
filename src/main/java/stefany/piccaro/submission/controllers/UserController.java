package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    // @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    // @GetMapping
    // public List<User> getUsers() {
    // return userService.getUsers();
    // }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") UUID userId, HttpServletRequest request) {
        // Get roles from token
        String token = jwtTools.getCurrentToken(request);
        int roles = jwtTools.getRolesFromToken(token);

        // Get target user info
        User targetUser = userService.findById(userId);

        // If attempting to access an admin user details and the requester is not an admin, deny access
        if (Role.hasRole(targetUser.getRoles(), Role.ADMIN)) {
            if (!Role.hasRole(roles, Role.ADMIN)) {
                throw new ForbiddenException("Access denied: insufficient permissions to view admin user details.");
            }
        }

        return userService.findById(userId);
    }

    @PostMapping(
            value = "/{userId}/upload-profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UserInfoDTO uploadProfilePicture(
            @PathVariable("userId") UUID userId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            HttpServletRequest request
    ) {
        // Get roles from token
        String token = jwtTools.getCurrentToken(request);
        UUID authUserId = jwtTools.getUserIDFromToken(token);
        int roles = jwtTools.getRolesFromToken(token);

        // If you're not an admin, you're not allowed to upload profile pictures for other users
        if (!Role.hasRole(roles, Role.ADMIN)) {
            if (!userId.equals(authUserId)) {
                throw new ForbiddenException("Access denied: insufficient permissions to upload profile picture for this user.");
            }
        }

        return userService.updateProfileImage(userId, file);
    }
}