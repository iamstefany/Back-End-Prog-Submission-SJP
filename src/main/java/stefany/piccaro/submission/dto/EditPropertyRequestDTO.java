package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record EditPropertyRequestDTO(
        @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
        String title,

        @Size(min = 5, max = 500, message = "Description should be between 5 and 500 characters.")
        String description,

        @Size(min = 5, max = 30, message = "Address should be between 5 and 30 characters.")
        String address,

        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero.")
        BigDecimal pricePerNight,

        @Min(value = 1, message = "Max. Guests must be greater than zero.")
        Integer maxGuests,

        Boolean automaticConfirmation,

        LocalTime checkInTime,

        LocalTime checkOutTime
) { }
