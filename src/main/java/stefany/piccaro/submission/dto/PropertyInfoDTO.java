package stefany.piccaro.submission.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PropertyInfoDTO (
        UUID propertyId,
        String listedBy,
        String title,
        String fullAddress) { }
