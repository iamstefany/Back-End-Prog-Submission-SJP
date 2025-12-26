package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SignUpRequestDTO(
        @NotBlank(message = "First name is mandatory.")
        @Size(min = 2, max = 30, message = "First name should be between 2 and 30 characters.")
        String firstName,

        @NotBlank(message = "Last name is mandatory.")
        @Size(min = 2, max = 30, message = "Last name should be between 2 and 30 characters.")
        String lastName,

        @NotBlank(message = "Email is mandatory.")
        @Email(message = "Email is not valid.")
        String email,

        @NotBlank(message = "Password is mandatory.")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
                message =
                "Password must be at least 8 characters long and contain at least one uppercase letter, " +
                "one lowercase letter, and one number."
        )
        String password,

        LocalDate dateOfBirth,

        String phoneNumber) { }
