package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;

public record CreatePropertyRequestDTO(
        @NotBlank(message = "Title is mandatory.")
        @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
        String title,

        @NotBlank(message = "Description is mandatory.")
        @Size(min = 5, max = 500, message = "Description should be between 5 and 500 characters.")
        String description,

        @NotBlank(message = "Address is mandatory.")
        @Size(min = 5, max = 30, message = "Address should be between 5 and 30 characters.")
        String address,

        @NotBlank(message = "City is mandatory.")
        @Pattern(
            regexp = "(?i)(Torino|Milano|Roma|Napoli)",
            message = "City can only be one of the following: Torino, Milano, Roma, Napoli."
        )
        String city,

        @NotBlank(message = "Country is mandatory.")
        @Pattern(regexp = "(?i)IT", message = "Country must be 'IT'.")
        String country,

        @NotNull(message = "Price per night is mandatory.")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero.")
        BigDecimal pricePerNight,

        @NotNull(message = "Max. Guests is mandatory.")
        @Min(value = 1, message = "Max. Guests must be greater than zero.")
        Integer maxGuests,

        Boolean automaticConfirmation,

        LocalTime checkInTime,

        LocalTime checkOutTime
) { }