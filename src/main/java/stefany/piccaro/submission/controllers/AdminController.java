package stefany.piccaro.submission.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stefany.piccaro.submission.dto.AuthInfoDTO;
import stefany.piccaro.submission.entities.Role;
import stefany.piccaro.submission.entities.User;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.security.JWTTools;
import stefany.piccaro.submission.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;


    // ------- Block a user -------
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{userId}/block")
    @ResponseStatus(HttpStatus.OK)
    public User blockUser(
            @PathVariable("userId") UUID userId,
            HttpServletRequest httpRequest) {
        // Get logged-in user info
        AuthInfoDTO authInfo = jwtTools.getAuthInfoFromHTTPRequest(httpRequest);

        // Get target user info
        User authUser = userService.findById(authInfo.userId());
        User targetUser = userService.findById(userId);

        // Prevent Basic Admin from blocking other Super Admin
        if (Role.hasRole(targetUser.getRoles(), Role.ADMIN)) {
            if (!authUser.getAdminProfile().getIsSuperAdmin() && targetUser.getAdminProfile().getIsSuperAdmin()) {
                throw new ForbiddenException("Access denied: you are not allowed to block a Super Admin.");
            }
        }

        return userService.blockUser(targetUser);
    }


    // ------- Unlock a user -------
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{userId}/unblock")
    @ResponseStatus(HttpStatus.OK)
    public User unblockUser(
            @PathVariable("userId") UUID userId,
            HttpServletRequest httpRequest) {
        User targetUser = userService.findById(userId);
        return userService.unblockUser(targetUser);
    }
}