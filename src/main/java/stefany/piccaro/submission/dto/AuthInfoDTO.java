package stefany.piccaro.submission.dto;


import stefany.piccaro.submission.entities.Role;

import java.util.List;
import java.util.UUID;

public record AuthInfoDTO(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        Boolean isBlocked,
        int roles,
        Boolean isSuperAdmin,
        String token
)
{
    // Convert roles integer to list of role names internally
    public List<String> roleNames() {
        return Role.getRoleNames(roles);
    }
}
