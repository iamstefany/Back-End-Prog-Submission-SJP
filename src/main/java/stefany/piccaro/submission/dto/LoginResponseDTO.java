package stefany.piccaro.submission.dto;

import java.util.UUID;

public record LoginResponseDTO(String accessToken, UUID userId) {
}
