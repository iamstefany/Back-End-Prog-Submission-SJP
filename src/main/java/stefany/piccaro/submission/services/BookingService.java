package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stefany.piccaro.submission.dto.CreateBookingRequestDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.repositories.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserService userService;

    @Transactional
    public Booking save(UUID propertyId, UUID userId, CreateBookingRequestDTO request) {
        Property property = propertyService.findById(propertyId);
        User user = userService.findById(userId);

        // Check availability in specified dates
        boolean isTaken = bookingRepository.existsOverlapping(
                propertyId,
                request.checkInDate(),
                request.checkOutDate()
        );

        if (isTaken) {
            throw new ForbiddenException("Property is not available for the selected dates.");
        }

        // Calculate total nights and price
        long nights = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
        if (nights <= 0) {
            throw new ValidationException("Check-out date must be after check-in date.");
        }
        BigDecimal totalPrice = property.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        // Create booking
        Booking booking = new Booking();
        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setProperty(property);
        booking.setUser(user);

        // Create payment
        Payment payment;
        switch (request.paymentType().toLowerCase()) {
            case "card" -> {
                if (request.cardNumber() == null || request.cardHolder() == null || request.cardExpiry() == null) {
                    throw new ValidationException("Card information is incomplete (required: cardNumber, cardHolder, cardExpiry).");
                }
                payment = new CardPayment(totalPrice, booking, request.cardNumber(), request.cardHolder(), request.cardExpiry());
            }
            case "paypal" -> {
                if (request.paypalEmail() == null || request.paypalTransactionId() == null) {
                    throw new ValidationException("Paypal information is incomplete (required: paypalEmail, paypalTransactionId).");
                }
                payment = new PaypalPayment(totalPrice, booking, request.paypalEmail(), request.paypalTransactionId());
            }
            default -> throw new ValidationException("Unsupported payment type.");
        }

        // Attach payment
        booking.getPayments().add(payment);

        String bookingStatus;
        String paymentStatus;

        if (property.isAutomaticConfirmation()) {
            bookingStatus = BookingStatus.CONFIRMED.name();
            paymentStatus = PaymentStatus.CONFIRMED.name();
        }
        else {
            bookingStatus = BookingStatus.PENDING.name();
            paymentStatus = PaymentStatus.ON_HOLD.name();
        }

        booking.setStatus(bookingStatus);
        payment.setStatus(paymentStatus);

        // Save booking and relevant payment
        return bookingRepository.save(booking);
    }
}