package stefany.piccaro.submission.dto;

import stefany.piccaro.submission.entities.User;

import java.util.List;
import java.util.UUID;

public record UserInfoDTO(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String profileImageUrl,
        List<String> roles
) {
    public static UserInfoDTO from(User user) {
        return new UserInfoDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getRoleNames()
        );
    }
}