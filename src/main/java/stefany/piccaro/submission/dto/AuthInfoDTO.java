package stefany.piccaro.submission.dto;

import java.util.List;
import java.util.UUID;

public record AuthInfoDTO(UUID userId, String firstName, String lastName, String email, boolean isBlocked, List<String> roleNames, String accessToken)
{ }
