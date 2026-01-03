package stefany.piccaro.submission.dto;

import jakarta.validation.constraints.*;
import stefany.piccaro.submission.entities.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateBookingRequestDTO(
        @NotNull(message = "Check-in date is required.")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required.")
        LocalDate checkOutDate,

        @NotNull(message = "Payment type is required.")
        @Pattern(
                regexp = "(?i)(Card|PayPal)",
                message = "Invalid payment type, payment methods accepted: 'Card' or 'PayPal'."
        )
        String paymentType,

        // Card payment info (only used if paymentType = CARD)
        String cardNumber,
        String cardHolder,
        String cardExpiry,

        // PayPal payment info (only used if paymentType = PAYPAL)
        String paypalEmail,
        String paypalTransactionId
) { }