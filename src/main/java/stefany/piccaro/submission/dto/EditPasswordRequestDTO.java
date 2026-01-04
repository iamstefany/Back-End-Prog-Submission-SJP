package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalTime;

public record EditPasswordRequestDTO(
        @NotBlank(message = "New password is mandatory.")
        String newPassword,

        String oldPassword
) { }
