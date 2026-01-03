package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EditUserRequestDTO(
        @Size(min = 2, max = 30, message = "First name should be between 2 and 30 characters.")
        String firstName,

        @Size(min = 2, max = 30, message = "Last name should be between 2 and 30 characters.")
        String lastName,

        LocalDate dateOfBirth,

        String phoneNumber) { }
