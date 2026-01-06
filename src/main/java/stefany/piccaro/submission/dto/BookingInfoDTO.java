package stefany.piccaro.submission.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BookingInfoDTO (
    UUID bookingId,
    String by,
    String status,
    LocalDate from,
    LocalDate to) { }
