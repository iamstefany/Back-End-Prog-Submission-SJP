package stefany.piccaro.submission.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import stefany.piccaro.submission.entities.Property;
import stefany.piccaro.submission.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateReviewRequestDTO(
        @NotNull(message = "Rating is mandatory.")
        @Min(value = 1, message = "The rating must be between 1 and 5.")
        @Max(value = 5, message = "The rating must be between 1 and 5.")
        Integer rating,

        @Size(min = 5, max = 100, message = "Comment should be between 5 and 100 characters.")
        String comment
) { }