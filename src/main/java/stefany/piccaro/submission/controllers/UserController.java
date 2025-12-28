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


    // ------- Get list of users -------
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getUsers();
    }


    // ------- Get user by ID (with role-based access control) -------
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") UUID userId, HttpServletRequest request) {
        // Get auth info from request
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromRequest(request);

        // Get target user info
        User targetUser = userService.findById(userId);

        // If attempting to access an admin user details and the requester is not an admin, deny access
        if (Role.hasRole(targetUser.getRoles(), Role.ADMIN)) {
            if (!Role.hasRole(authInfo.roles(), Role.ADMIN)) {
                throw new ForbiddenException("Access denied: insufficient permissions to view admin user details.");
            }
        }

        return userService.findById(userId);
    }


    // ------- Upload profile picture -------
    @PostMapping(
            value = "/{userId}/upload-profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UploadProfilePictureDTO uploadProfilePicture(
            @PathVariable("userId") UUID userId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            HttpServletRequest request
    ) {
        // Get roles from token
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromRequest(request);

        // If you're not an admin, you're not allowed to upload profile pictures for other users
        if (!Role.hasRole(authInfo.roles(), Role.ADMIN)) {
            if (!userId.equals(authInfo.userId())) {
                throw new ForbiddenException("Access denied: insufficient permissions to upload profile picture for this user.");
            }
        }

        return new UploadProfilePictureDTO(userService.updateProfileImage(userId, file));
    }
}