package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stefany.piccaro.submission.dto.CreateBookingRequestDTO;
import stefany.piccaro.submission.entities.*;
import stefany.piccaro.submission.exceptions.ForbiddenException;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.exceptions.ValidationException;
import stefany.piccaro.submission.repositories.BookingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private ExchangeRateService exchangeRateService;

    public Booking findById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(bookingId));
    }

    public List<Booking> findByUserId(UUID userId) {
        return bookingRepository.findByUser_UserId(userId);
    }

    public boolean existsCompletedBooking(UUID userId, UUID propertyId) {
        return bookingRepository.existsCompletedBooking(userId, propertyId);
    }

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

        // Calculate total nights
        long nights = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
        if (nights <= 0) {
            throw new ValidationException("Check-out date must be after check-in date.");
        }

        // Price EUR
        BigDecimal totalPriceEUR = property.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        // Get exchange rate
        BigDecimal rate = exchangeRateService.getRateFromEur(request.currency());

        // Convert to user's currency
        BigDecimal amountCharged = totalPriceEUR.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        // Create booking
        Booking booking = new Booking();
        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());
        booking.setTotalPrice(totalPriceEUR);
        booking.setProperty(property);
        booking.setUser(user);

        // Create payment
        Payment payment;
        switch (request.paymentType().toLowerCase()) {
            case "card" -> {
                if (request.cardNumber() == null || request.cardHolder() == null || request.cardExpiry() == null) {
                    throw new ValidationException("Card information is incomplete (required: cardNumber, cardHolder, cardExpiry).");
                }
                payment = new CardPayment(
                        request.currency(),
                        amountCharged,
                        totalPriceEUR,
                        booking,
                        request.cardNumber(),
                        request.cardHolder(),
                        request.cardExpiry()
                );
            }
            case "paypal" -> {
                if (request.paypalEmail() == null || request.paypalTransactionId() == null) {
                    throw new ValidationException("Paypal information is incomplete (required: paypalEmail, paypalTransactionId).");
                }
                payment = new PaypalPayment(
                        request.currency(),
                        amountCharged,
                        totalPriceEUR,
                        booking,
                        request.paypalEmail(),
                        request.paypalTransactionId()
                );
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

    @Transactional
    public Booking approveBooking(UUID bookingId, UUID userId) {
        // Check if property belongs to user
        Booking booking = findById(bookingId);
        if (!booking.getProperty().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Access Denied. You are not the property's owner for this booking.");
        }

        // Save and return booking
        booking.setStatus(BookingStatus.CONFIRMED.name());

        // Mark all payments for this booking as confirmed
        if (booking.getPayments() != null) {
            booking.getPayments().forEach(payment -> payment.setStatus(PaymentStatus.CONFIRMED.name()));
        }

        // Save booking and relevant payments
        return bookingRepository.save(booking);
    }

    @Transactional
    public void rejectBooking(UUID bookingId, UUID userId) {
        // Check if property belongs to user
        Booking booking = findById(bookingId);
        if (!booking.getProperty().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Access Denied. You are not the property's owner for this booking.");
        }

        // Prevent user from rejecting a booking that is already confirmed
        if (booking.getStatus().equalsIgnoreCase(BookingStatus.CONFIRMED.name())) {
            throw new ForbiddenException("You are not allowed to reject a booking that is already confirmed.");
        }

        // Delete booking and relevant payments
        bookingRepository.delete(booking);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        Booking booking = findById(bookingId);

        bookingRepository.delete(booking);
    }
}